package com.example.articleappassignment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.articleappassignment.adapter.NewsAdapter
import com.example.articleappassignment.databinding.ActivityMainBinding
import com.example.articleappassignment.models.Article
import com.example.articleappassignment.repository.NewsRepository
import com.example.articleappassignment.utils.Constants
import com.example.articleappassignment.utils.Constants.Companion.NEW_TO_OLD
import com.example.articleappassignment.utils.Constants.Companion.SORT_BY
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: NewsViewModel by viewModels { NewsViewModelFactory(repository) }
    private val repository = NewsRepository()
    private var adapter : NewsAdapter? = null
    private lateinit var articles : MutableList<Article>
    private var sortBy : Int = 0
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var filters : Array<String>
    private var sortByAdapter :  ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dataStore = createDataStore(SORT_BY)
        filters = resources.getStringArray(R.array.filters)

        lifecycleScope.launch {
            var value = readDataStore(SORT_BY)
            if (TextUtils.isEmpty(value)){
                setDataStore(SORT_BY, NEW_TO_OLD)
                value = NEW_TO_OLD
            }
            sortBy = value!!.toInt()
            if (sortByAdapter == null){
                setupSortByAdapter()
            }
            viewModel.fetchNewsArticles()
        }
        binding.sortText.layout
        binding.sortText.onItemClickListener = this


        viewModel.newsLiveData.observe(this) {
            it?.let { newsResponse ->
                newsResponse.articles.let { newArticles ->
                    setUpRecyclerView(newArticles)
                    binding.progressbar.visibility = View.GONE
                    binding.dropdown.visibility = View.VISIBLE
                    if (sortByAdapter == null){
                        setupSortByAdapter()
                    }
                }
            }
        }


    }

    private fun updateNewsAdapter(newArticles: List<Article>) {
        adapter?.submitList(newArticles)
        adapter?.notifyDataSetChanged()
    }

    private suspend fun setDataStore(key : String, value : String){
        val datastoreKey = preferencesKey<String>(key)
        dataStore.edit {
            it[datastoreKey] = value
        }
    }

    private suspend fun readDataStore(key : String) : String? {
        val datastoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[datastoreKey]
    }

    private fun setUpRecyclerView(newArticles: MutableList<Article>) {
        if (sortBy == 0){
            articles = sortByDescending(newArticles)
        } else {
            articles = sortByAscending(newArticles)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this, articles.toMutableList(), this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSortByAdapter() {
        binding.dropdown.editText?.setText(filters[sortBy])
        sortByAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, listOf(filters[1 - sortBy]))
        binding.sortText.setAdapter(sortByAdapter)
    }

    private fun sortByAscending(listOfArticles : List<Article>) : MutableList<Article> {
        return listOfArticles.sortedBy {
            Constants.convertPublishedTimeToMilliseconds(it.publishedAt)
        }.toMutableList()
    }


    private fun sortByDescending(listOfArticles : List<Article>) : MutableList<Article> {
        return listOfArticles.sortedByDescending {
            Constants.convertPublishedTimeToMilliseconds(it.publishedAt)
        }.toMutableList()
    }

    override fun onItemClick(position: Int, articles: List<Article>) {
        val url = articles[position].url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        sortBy = 1 - sortBy
        setupSortByAdapter()
        lifecycleScope.launch {
            setDataStore(SORT_BY,sortBy.toString())
            if(sortBy == 0){
                updateNewsAdapter(sortByDescending(articles))
            } else {
                updateNewsAdapter(sortByAscending(articles))
            }
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }
}