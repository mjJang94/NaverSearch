package com.mj.naversearch.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mj.naversearch.util.event
import com.mj.naversearch.util.hide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.Int
import kotlin.String
import kotlin.coroutines.CoroutineContext
import com.mj.naversearch.ui.result.ResultCategoryAdapter as Result

class ResultViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    enum class Category(val title: String) {
        News("뉴스"),
        Books("도서"),
        Encyclopedia("백과사전"),
        QnA("지식in"),
        Image("이미지");

        companion object {
            operator fun get(raw: Int?): Category =
                values().firstOrNull { it.ordinal == raw } ?: News
        }
    }

    sealed class ResultEvent {
        object Back : ResultEvent()
        data class Searching(val query: String?) : ResultEvent()
    }

    private val _keyword = MutableLiveData<String>()
    val keyword = _keyword.hide()
    fun configure(query: String) {
        _keyword.postValue(query)
        _selectedCategoryInfo.postValue(query to 0)
    }

    private val _uiEvent = MutableSharedFlow<ResultEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun back() {
        launch {
            _uiEvent.emit(ResultEvent.Back)
        }
    }

    fun searching() {
        launch {
            val query = _keyword.value
            _uiEvent.emit(ResultEvent.Searching(query))
        }
    }

    fun delete() {
        launch {
            _uiEvent.emit(ResultEvent.Searching(null))
        }
    }

    private val _moreCategory = MutableLiveData(false)
    val moreCategory = _moreCategory.hide()
    fun more() {
        val more = _moreCategory.value ?: return
        _moreCategory.postValue(!more)
    }

    private val _openLink = MutableLiveData<String>()
    val openLink = _openLink.event()
    fun openLink(link: String) {
        _openLink.postValue(link)
    }

    private val _selectedCategoryInfo = MutableLiveData<Pair<String, Int>>()
    val selectedCategoryInfo = _selectedCategoryInfo.event()

    private val _searchCategory = MutableLiveData<List<String>>(
        mutableListOf<String>().apply {
            (0..5).forEach { index ->
                add(Category[index].title)
            }
        }
    )
    val searchCategoryItem = _searchCategory.map { categories ->
        categories.mapIndexed { id, name ->
            id.formalize(name)
        }
    }

    private fun Int.formalize(name: String) = Result.CategoryItem(
        id = this,
        name = name,
        selected = _selectedCategoryInfo.map { (_, id) -> id == this }
    )

    val categoryCallback = object : Result.Callback {
        override fun toggle(id: Int) {
            val query = _keyword.value ?: return
            _selectedCategoryInfo.postValue(query to id)
        }
    }
}