package com.mj.naversearch.ui.result.books

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
import com.mj.domain.model.books.BookData
import com.mj.naversearch.R
import com.mj.naversearch.databinding.RowBooksItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BooksAdapter(
    private val callback: Callback
) : PagingDataAdapter<BookData, BooksAdapter.BooksViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean = oldItem == newItem
    }

    interface Callback {
        val coroutineScope: CoroutineScope
        fun onLoadState(size: Int, state: CombinedLoadStates)
        fun click(item: BookData)
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("bookItems", "callback")
        fun RecyclerView.setItems(data: PagingData<BookData>?, callback: Callback) {
            val adapter = adapter as? BooksAdapter ?: BooksAdapter(callback)
                .apply { addLoadStateListener { callback.onLoadState(itemCount, it) } }
                .also { adapter = it }
            callback.coroutineScope.launch(Dispatchers.Default) {
                adapter.submitData(data ?: return@launch)
            }
        }
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.update(getItem(position) ?: return)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowBooksItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_books_item, this, false
        ).apply { lifecycleOwner = findViewTreeLifecycleOwner() }
        return BooksViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    class BooksViewHolder(itemView: View, val update: (item: BookData?) -> Unit) : ViewHolder(itemView)
}
