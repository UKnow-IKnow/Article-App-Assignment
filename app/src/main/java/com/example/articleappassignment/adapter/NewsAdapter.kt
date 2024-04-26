package com.example.articleappassignment.adapter

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.articleappassignment.R
import com.example.articleappassignment.databinding.ItemNewsBinding
import com.example.articleappassignment.models.Article
import com.example.articleappassignment.utils.Constants

class NewsAdapter(
    private val context: Context,
    private var newsList: MutableList<Article>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, listener, newsList)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        if (!TextUtils.isEmpty(newsItem.publishedAt)) {
            holder.binding.publishTime.visibility = View.VISIBLE
            holder.binding.publishTime.text =
                Constants.getFormattedDateAndTime(newsItem.publishedAt)
        } else {
            holder.binding.publishTime.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(newsItem.title)) {
            holder.binding.title.visibility = View.VISIBLE
            holder.binding.title.text = newsItem.title
        } else {
            holder.binding.title.visibility = View.GONE
        }


        if (!TextUtils.isEmpty(newsItem.author)) {
            holder.binding.AuthorName.visibility = View.VISIBLE
            holder.binding.AuthorName.text = "Author : ".plus(newsItem.author)
        } else {
            holder.binding.AuthorName.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(newsItem.description)) {
            holder.binding.description.visibility = View.VISIBLE
            holder.binding.description.text = newsItem.description
        } else {
            holder.binding.description.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(newsItem.urlToImage)) {
            holder.binding.newsImage.visibility = View.VISIBLE
            Glide.with(context).load(newsItem.urlToImage).placeholder(R.drawable.ic_download)
                .error(R.drawable.image_not_found).into(holder.binding.newsImage)
        } else {
            holder.binding.newsImage.visibility = View.GONE
        }
    }

    fun submitList(newArticles: List<Article>) {
        this.newsList.clear()
        this.newsList.addAll(newArticles)
    }


    class NewsViewHolder(
        val binding: ItemNewsBinding,
        private val listener: OnItemClickListener,
        private val newsList: MutableList<Article>
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, newsList)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, articles: List<Article>)
    }
}