package com.aston.news.presentation.adapters.sources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aston.news.R
import com.aston.news.core.network.NetworkUtils
import com.aston.news.databinding.SourceItemBinding
import com.aston.news.domain.Source
import com.bumptech.glide.Glide

class SourcesAdapter (
    private val onClickSource: (Source) -> Unit
): ListAdapter<Source, SourcesAdapter.ViewHolder>(DiffUtil()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = SourceItemBinding.bind(view)

        fun bind(item: Source) {
            Glide
                .with(itemView.context)
                .load(NetworkUtils.getSourceIconUrl(item.url))
                .into(binding.src)

            binding.nameSource.text = item.name
            binding.description.text = "${item.category.uppercase()} | ${item.country.uppercase()}"
            binding.root.setOnClickListener { onClickSource.invoke(item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.source_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

}

class DiffUtil():  DiffUtil.ItemCallback<Source>() {
    override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
        return oldItem == newItem
    }

}