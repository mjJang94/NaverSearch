package com.mj.naversearch.ui.result.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mj.domain.model.news.NewsData
import com.mj.naversearch.R
import com.mj.naversearch.databinding.RowNewsItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsAdapter(
    private val callback: Callback
) : PagingDataAdapter<NewsData, NewsAdapter.ResultContentViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData) = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData) = oldItem == newItem
    }

    interface Callback {
        val coroutineScope: CoroutineScope
        fun click(item: NewsData)
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("resultContentItem", "callback")
        fun RecyclerView.setItems(data: PagingData<NewsData>?, callback: Callback) {
            val adapter = adapter as? NewsAdapter
                ?: NewsAdapter(callback)
                    .also { adapter = it }
            callback.coroutineScope.launch(Dispatchers.Default) {
                adapter.submitData(data ?: return@launch)
            }
        }
    }

    override fun onBindViewHolder(holder: ResultContentViewHolder, position: Int) {
        holder.update(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultContentViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowNewsItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_news_item, this, false
        ).apply { lifecycleOwner = findViewTreeLifecycleOwner() }
        return ResultContentViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    class ResultContentViewHolder(itemView: View, val update: (item: NewsData?) -> Unit) : ViewHolder(itemView)
}