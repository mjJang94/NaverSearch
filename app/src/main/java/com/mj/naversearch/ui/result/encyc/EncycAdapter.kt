package com.mj.naversearch.ui.result.encyc

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
import com.mj.domain.model.encyc.EncycData
import com.mj.domain.model.news.NewsData
import com.mj.naversearch.R
import com.mj.naversearch.databinding.RowEncycItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EncycAdapter(
    private val callback: Callback
) : PagingDataAdapter<EncycData, EncycAdapter.EncycViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<EncycData>() {
        override fun areItemsTheSame(oldItem: EncycData, newItem: EncycData) = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: EncycData, newItem: EncycData) = oldItem == newItem
    }

    interface Callback {
        val coroutineScope: CoroutineScope
        fun onLoadState(size: Int, state: CombinedLoadStates)
        fun click(item: EncycData)
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("encycItems", "callback")
        fun RecyclerView.setItems(data: PagingData<EncycData>?, callback: Callback) {
            val adapter = adapter as? EncycAdapter ?: EncycAdapter(callback)
                .apply { addLoadStateListener { callback.onLoadState(itemCount, it) } }
                .also { adapter = it }
            callback.coroutineScope.launch(Dispatchers.Default) {
                adapter.submitData(data ?: return@launch)
            }
        }
    }

    override fun onBindViewHolder(holder: EncycViewHolder, position: Int) {
        holder.update(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncycViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowEncycItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_encyc_item, this, false
        ).apply { lifecycleOwner = findViewTreeLifecycleOwner() }

        return EncycViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    class EncycViewHolder(itemView: View, val update: (item: EncycData?) -> Unit) : ViewHolder(itemView)
}