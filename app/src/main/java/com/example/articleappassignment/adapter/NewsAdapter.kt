package com.example.articleappassignment.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.articleappassignment.R
import com.example.articleappassignment.databinding.ItemNewsBinding
import com.example.articleappassignment.models.Article
import com.example.articleappassignment.utils.Constants


/**
 * NewsAdapter for displaying news articles in a RecyclerView.
 * @param context The context in which the adapter is being used.
 * @param newsList The list of articles to be displayed.
 * @param listener The click listener for handling item clicks.
 */
class NewsAdapter(
    private val context: Context,
    private var newsList: MutableList<Article>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    /**
     * Called when RecyclerView needs a new [NewsViewHolder] of the given type to represent
     * an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new [NewsViewHolder] that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, listener, newsList)
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return newsList.size
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the [holder] to reflect the item at the given [position].
     * @param holder The ViewHolder which should be updated to represent the contents of the item at
     * the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        // Show or hide publish time based on whether it is empty or not.
        if (!TextUtils.isEmpty(newsItem.publishedAt)) {
            holder.binding.publishTime.visibility = View.VISIBLE
            holder.binding.publishTime.text =
                Constants.getFormattedDateAndTime(newsItem.publishedAt)
        } else {
            holder.binding.publishTime.visibility = View.GONE
        }

        // Show or hide title based on whether it is empty or not.
        if (!TextUtils.isEmpty(newsItem.title)) {
            holder.binding.title.visibility = View.VISIBLE
            holder.binding.title.text = newsItem.title
        } else {
            holder.binding.title.visibility = View.GONE
        }

        // Show or hide author name based on whether it is empty or not.
        if (!TextUtils.isEmpty(newsItem.author)) {
            holder.binding.AuthorName.visibility = View.VISIBLE
            holder.binding.AuthorName.text = "Author : ".plus(newsItem.author)
        } else {
            holder.binding.AuthorName.visibility = View.GONE
        }

        // Show or hide description based on whether it is empty or not.
        if (!TextUtils.isEmpty(newsItem.description)) {
            holder.binding.description.visibility = View.VISIBLE
            holder.binding.description.text = newsItem.description
        } else {
            holder.binding.description.visibility = View.GONE
        }

        // Show or hide news image based on whether it is empty or not, and load image using Glide.
        if (!TextUtils.isEmpty(newsItem.urlToImage)) {
            holder.binding.newsImage.visibility = View.VISIBLE
            Glide.with(context).load(newsItem.urlToImage).placeholder(R.drawable.ic_download)
                .error(R.drawable.image_not_found).into(holder.binding.newsImage)
        } else {
            holder.binding.newsImage.visibility = View.GONE
        }
    }

    /**
     * Clears the current list of articles and replaces it with the new list provided.
     * @param newArticles The new list of articles to be displayed.
     */
    fun submitList(newArticles: List<Article>) {
        this.newsList.clear()
        this.newsList.addAll(newArticles)
    }


    /**
     * ViewHolder class for holding views of individual news articles in the RecyclerView.
     * @param binding The data binding object associated with the item layout.
     * @param listener The click listener for handling item clicks.
     * @param newsList The list of articles associated with the adapter.
     */
    class NewsViewHolder(
        val binding: ItemNewsBinding,
        private val listener: OnItemClickListener,
        private val newsList: MutableList<Article>
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * Called when the item view is clicked.
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, newsList)
            }
        }

    }

    /**
     * Interface definition for a callback to be invoked when a news article item is clicked.
     */
    interface OnItemClickListener {
        /**
         * Called when a news article item is clicked.
         * @param position The position of the clicked item in the RecyclerView.
         * @param articles The list of articles associated with the adapter.
         */
        fun onItemClick(position: Int, articles: List<Article>)
    }
}