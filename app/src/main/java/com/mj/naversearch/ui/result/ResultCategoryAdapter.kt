package com.mj.naversearch.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mj.naversearch.R
import com.mj.naversearch.databinding.RowResultCategoryItemBinding
import com.mj.naversearch.ui.result.ResultCategoryAdapter.CategoryItem
import com.mj.naversearch.ui.result.ResultCategoryAdapter.ViewHolder
import com.mj.naversearch.util.ConsistInLiveData
import com.mj.naversearch.util.consistIn

class ResultCategoryAdapter(
    private val callback: Callback
) : ListAdapter<CategoryItem, ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem) = oldItem == newItem
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("resultCategoryItems", "callback")
        fun RecyclerView.setItems(items: List<CategoryItem>?, callback: Callback) {
            val adapter = adapter as? ResultCategoryAdapter
                ?: ResultCategoryAdapter(callback).also { adapter = it }
            adapter.submitList(items ?: return)
        }
    }

    interface Callback {
        fun toggle(id: Int)
    }

    class ViewHolder(itemView: View, val update: (CategoryItem) -> Unit): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowResultCategoryItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_result_category_item, this, false
        ).apply { lifecycleOwner = parent.findViewTreeLifecycleOwner() }
        return ViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(getItem(position))
    }

    data class CategoryItem(
        val id: Int,
        val name: String,
        val selected: ConsistInLiveData<Boolean>
    ) {
        constructor(
            id: Int,
            name: String,
            selected: LiveData<Boolean>,
        ) : this(id, name, selected.consistIn(id))
    }
}