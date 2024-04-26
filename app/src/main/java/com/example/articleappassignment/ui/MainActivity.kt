package com.example.articleappassignment.ui


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.articleappassignment.NewsViewModel
import com.example.articleappassignment.NewsViewModelFactory
import com.example.articleappassignment.R
import com.example.articleappassignment.adapter.NewsAdapter
import com.example.articleappassignment.databinding.ActivityMainBinding
import com.example.articleappassignment.models.Article
import com.example.articleappassignment.repository.NewsRepository
import com.example.articleappassignment.utils.Constants
import com.example.articleappassignment.utils.Constants.Companion.NEW_TO_OLD
import com.example.articleappassignment.utils.Constants.Companion.SORT_BY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * MainActivity responsible for displaying news articles and managing user interactions.
 *
 * This activity displays a list of news articles fetched from a remote data source. Users can
 * interact with the articles by selecting them, and they can also change the sorting order of
 * articles based on their preference. Additionally, the activity handles requesting notification
 * permissions if not granted.
 */
class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener,
    AdapterView.OnItemClickListener {

    // View binding instance for accessing views in the layout
    private lateinit var binding: ActivityMainBinding

    // Request code for notification permission request
    private val notificationPermissionRequestCode = 1001

    // ViewModel for managing news data and operations
    private val viewModel: NewsViewModel by viewModels { NewsViewModelFactory(repository) }

    // Repository for providing news data
    private val repository = NewsRepository()

    // Adapter for displaying news articles in a RecyclerView
    private var adapter: NewsAdapter? = null

    // List of news articles retrieved from the data source
    private lateinit var articles: MutableList<Article>

    // Integer representing the current sorting order of articles
    private var sortBy: Int = 0

    // DataStore for storing sorting preference
    private lateinit var dataStore: DataStore<Preferences>

    /**
     * Array containing the available filters for sorting news articles.
     * These filters represent different sorting options such as "Newest to Oldest" or "Oldest to Newest".
     */
    private lateinit var filters: Array<String>

    /**
     * Adapter for displaying sorting options in a dropdown menu.
     * This adapter is used to populate the dropdown menu with sorting options based on the available filters.
     */
    private var sortByAdapter: ArrayAdapter<String>? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dataStore = createDataStore(SORT_BY)
        filters = resources.getStringArray(R.array.filters)

        /**
         * Checks if notification permissions are granted. If not, requests notification permission.
         *
         * @return `true` if notification permissions are already granted, `false` otherwise.
         */
        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission()
        }


        /**
         * Coroutine block for initializing sorting preferences and fetching news articles.
         *
         * This coroutine block reads the sorting preference from the data store. If the preference is
         * empty, it sets the default sorting preference to "Newest to Oldest" and retrieves it. It then
         * initializes the sorting order, sets up the sorting adapter if it's not already initialized, and
         * fetches news articles using the ViewModel. This block is launched in the lifecycle scope of the
         * activity.
         */
        lifecycleScope.launch {
            var value = readDataStore(SORT_BY)
            if (TextUtils.isEmpty(value)) {
                setDataStore(SORT_BY, NEW_TO_OLD)
                value = NEW_TO_OLD
            }
            sortBy = value!!.toInt()
            if (sortByAdapter == null) {
                setupSortByAdapter()
            }
            viewModel.fetchNewsArticles()
        }

        /**
         * Accesses the layout property of the 'sortText' view.
         */
        binding.sortText.layout
        //Sets an OnItemClickListener to the 'sortText' view.
        binding.sortText.onItemClickListener = this


        /**
         * Observes changes in the newsLiveData and updates the UI accordingly.
         *
         * This block observes changes in the newsLiveData LiveData within the ViewModel. When a new value
         * is emitted, it checks if the emitted value is not null. If it's not null, it extracts the list
         * of articles from the NewsResponse and sets up the RecyclerView with the new articles. It also
         * stops the shimmer animation, hides the shimmer layout, shows the dropdown menu, and sets up
         * the sorting adapter if it's not already initialized. This block updates the UI whenever new
         * news data is received.
         */
        viewModel.newsLiveData.observe(this) {
            it?.let { newsResponse ->
                newsResponse.articles.let { newArticles ->
                    setUpRecyclerView(newArticles)
                    binding.shimmerLayout.apply {
                        stopShimmer()
                        visibility = View.GONE
                    }
                    binding.dropdown.visibility = View.VISIBLE
                    if (sortByAdapter == null) {
                        setupSortByAdapter()
                    }
                }
            }
        }


    }


    /**
     * Updates the news adapter with new articles.
     *
     * This function updates the RecyclerView adapter with a new list of articles. It submits the new
     * list to the adapter using the submitList() method, and then notifies the adapter that the data
     * set has changed using the notifyDataSetChanged() method. This ensures that the RecyclerView
     * reflects the updated list of articles.
     *
     * @param newArticles The new list of articles to be displayed.
     */
    private fun updateNewsAdapter(newArticles: List<Article>) {
        adapter?.submitList(newArticles)
        adapter?.notifyDataSetChanged()
    }


    /**
     * Suspended function to set a value in the data store.
     *
     * This function sets the specified key-value pair in the data store. It uses the provided key
     * to create a preferences key and then updates the data store by invoking the edit() function
     * within a coroutine context. This function suspends until the data store is successfully edited.
     *
     * @param key The key under which to store the value.
     * @param value The value to be stored in the data store.
     */
    private suspend fun setDataStore(key: String, value: String) {
        val datastoreKey = preferencesKey<String>(key)
        dataStore.edit {
            it[datastoreKey] = value
        }
    }


    /**
     * Suspended function to read a value from the data store.
     *
     * This function retrieves the value associated with the specified key from the data store. It
     * uses the provided key to create a preferences key and then retrieves the value from the data
     * store by accessing the first element of the data flow. This function suspends until the value
     * is successfully read from the data store.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the specified key, or null if the key is not found.
     */
    private suspend fun readDataStore(key: String): String? {
        val datastoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[datastoreKey]
    }


    /**
     * Sets up the RecyclerView with the list of articles.
     *
     * This function configures the RecyclerView with the provided list of articles. Depending on the
     * current sorting order (determined by the sortBy variable), it sorts the articles either in
     * descending or ascending order. It sets the layout manager and adapter for the RecyclerView.
     *
     * @param newArticles The list of articles to be displayed in the RecyclerView.
     */
    private fun setUpRecyclerView(newArticles: MutableList<Article>) {

        // Sort the articles based on the current sorting order
        articles = if (sortBy == 0) {
            sortByDescending(newArticles)
        } else {
            sortByAscending(newArticles)
        }

        // Set up RecyclerView layout manager and adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this, articles.toMutableList(), this)
        binding.recyclerView.adapter = adapter
    }


    /**
     * Sets up the adapter for the sorting dropdown menu.
     *
     * This function initializes the adapter for the sorting dropdown menu (referred to as 'dropdown'
     * in the binding). It sets the text of the dropdown to the currently selected sorting option.
     * Then, it creates an ArrayAdapter with the available sorting options and sets it as the adapter
     * for the sorting text field (referred to as 'sortText' in the binding).
     */
    private fun setupSortByAdapter() {
        // Set text of dropdown to the currently selected sorting option
        binding.dropdown.editText?.setText(filters[sortBy])

        // Create ArrayAdapter with available sorting options
        sortByAdapter = ArrayAdapter(
            this@MainActivity, android.R.layout.simple_list_item_1, listOf(filters[1 - sortBy])
        )

        // Set ArrayAdapter as the adapter for the sorting text field
        binding.sortText.setAdapter(sortByAdapter)
    }


    /**
     * Sorts the list of articles in ascending order based on their published time.
     *
     * This function sorts the given list of articles in ascending order based on their published
     * time. It uses the convertPublishedTimeToMilliseconds() function from the Constants class to
     * extract the published time of each article and sorts them accordingly.
     *
     * @param listOfArticles The list of articles to be sorted.
     * @return A new list of articles sorted in ascending order of published time.
     */
    private fun sortByAscending(listOfArticles: List<Article>): MutableList<Article> {
        return listOfArticles.sortedBy {
            Constants.convertPublishedTimeToMilliseconds(it.publishedAt)
        }.toMutableList()
    }


    /**
     * Sorts the list of articles in descending order based on their published time.
     *
     * This function sorts the given list of articles in descending order based on their published
     * time. It uses the convertPublishedTimeToMilliseconds() function from the Constants class to
     * extract the published time of each article and sorts them accordingly.
     *
     * @param listOfArticles The list of articles to be sorted.
     * @return A new list of articles sorted in descending order of published time.
     */
    private fun sortByDescending(listOfArticles: List<Article>): MutableList<Article> {
        return listOfArticles.sortedByDescending {
            Constants.convertPublishedTimeToMilliseconds(it.publishedAt)
        }.toMutableList()
    }


    /**
     * Handles item clicks in the RecyclerView.
     *
     * This method is invoked when an item in the RecyclerView is clicked. It extracts the URL of
     * the clicked article, creates an intent to view the URL, and starts the activity to view the
     * article in a web browser.
     *
     * @param position The position of the clicked item in the RecyclerView.
     * @param articles The list of articles displayed in the RecyclerView.
     */
    override fun onItemClick(position: Int, articles: List<Article>) {
        val url = articles[position].url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }


    /**
     * Handles item clicks in the sorting dropdown menu.
     *
     * This method is invoked when an item in the sorting dropdown menu is clicked. It toggles the
     * sorting order, updates the sorting adapter, updates the sorting preference in the data store,
     * and refreshes the RecyclerView with the updated sorting order. It also scrolls the RecyclerView
     * to the top position after the update.
     *
     * @param parent The AdapterView where the click happened.
     * @param view The clicked view within the AdapterView.
     * @param position The position of the clicked item in the AdapterView.
     * @param id The row id of the clicked item.
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sortBy = 1 - sortBy
        setupSortByAdapter()
        lifecycleScope.launch {
            setDataStore(SORT_BY, sortBy.toString())
            if (sortBy == 0) {
                updateNewsAdapter(sortByDescending(articles))
            } else {
                updateNewsAdapter(sortByAscending(articles))
            }
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }


    /**
     * Checks if notification permission is granted.
     *
     * This function checks whether the notification permission is granted to the application by
     * verifying the result of ContextCompat.checkSelfPermission(). It is annotated with
     * @RequiresApi(Build.VERSION_CODES.TIRAMISU) to indicate that it requires the TIRAMISU version of
     * the Android SDK or higher.
     *
     * @return `true` if notification permission is granted, `false` otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotificationPermissionGranted(): Boolean {
        // Check if notification permission is granted
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * Requests notification permission from the user.
     *
     * This function uses the ActivityCompat.requestPermissions() method to request the notification
     * permission from the user. It is annotated with @RequiresApi(Build.VERSION_CODES.TIRAMISU) to
     * indicate that it requires the TIRAMISU version of the Android SDK or higher.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            notificationPermissionRequestCode
        )
    }


    /**
     * Callback method invoked when the user responds to a permission request.
     *
     * This method is called after the user responds to a permission request initiated by the
     * ActivityCompat.requestPermissions() method. It checks if the request code matches the
     * notification permission request code. If so, it checks the grant result to determine whether
     * notification permission was granted or denied. If permission is denied, it displays a toast
     * message informing the user about the denial.
     *
     * @param requestCode The request code passed to ActivityCompat.requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Notification permission granted, proceed with your app's functionality
            } else {
                // Notification permission denied, show a toast message
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

}