package com.aston.news.presentation.adapters.headlines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aston.news.R
import com.aston.news.databinding.ArticleItemBinding
import com.bumptech.glide.Glide

class HeadlinesAdapter (
    private val onClickArticle: (HeadlineItem) -> Unit
): ListAdapter<HeadlineItem, HeadlinesAdapter.ViewHolder>(DiffUtil()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ArticleItemBinding.bind(view)

        fun bind(item: HeadlineItem) {
            Glide.with(itemView.context)
                .load(item.src)
                .placeholder(R.drawable.news_placeholder)
                .into(binding.src)

            Glide.with(itemView.context)
                .load(item.sourceSrc)
                .circleCrop()
                .into(binding.srcSource)

            binding.nameSource.text = item.sourceName
            binding.description.text = item.title

            binding.root.setOnClickListener { onClickArticle.invoke(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}

class DiffUtil: DiffUtil.ItemCallback<HeadlineItem>() {

    override fun areItemsTheSame(oldItem: HeadlineItem, newItem: HeadlineItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HeadlineItem, newItem: HeadlineItem): Boolean {
        return oldItem == newItem
    }

}