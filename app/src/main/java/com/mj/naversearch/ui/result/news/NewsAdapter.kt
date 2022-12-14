package com.mj.naversearch.ui.result.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.CombinedLoadStates
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
) : PagingDataAdapter<NewsData, NewsAdapter.NewsViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData) = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData) = oldItem == newItem
    }

    interface Callback {
        val coroutineScope: CoroutineScope
        fun onLoadState(size: Int, state: CombinedLoadStates)
        fun click(item: NewsData)
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("newItems", "callback")
        fun RecyclerView.setItems(data: PagingData<NewsData>?, callback: Callback) {
            val adapter = adapter as? NewsAdapter ?: NewsAdapter(callback)
                .apply { addLoadStateListener { callback.onLoadState(itemCount, it) } }
                .also { adapter = it }
            callback.coroutineScope.launch(Dispatchers.Default) {
                adapter.submitData(data ?: return@launch)
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.update(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowNewsItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_news_item, this, false
        ).apply { lifecycleOwner = findViewTreeLifecycleOwner() }
        return NewsViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    class NewsViewHolder(itemView: View, val update: (item: NewsData?) -> Unit) : ViewHolder(itemView)
}