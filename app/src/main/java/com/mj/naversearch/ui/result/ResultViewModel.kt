package com.mj.naversearch.ui.result

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mj.domain.model.news.NewsData
import com.mj.domain.usecase.GetRemoteSearchUseCase
import com.mj.naversearch.data.NaverSearchDataSource
import com.mj.naversearch.util.event
import com.mj.naversearch.util.hide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.Int
import kotlin.String
import kotlin.arrayOf
import kotlin.coroutines.CoroutineContext
import com.mj.naversearch.ui.result.ResultCategoryAdapter as Result
import com.mj.naversearch.ui.result.news.NewsAdapter as Content

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getRemoteSearchUseCase: GetRemoteSearchUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    sealed class ResultEvent {
        object Back : ResultEvent()
        data class Searching(val query: String?) : ResultEvent()
    }

    private val _keyword = MutableLiveData<String>()
    val keyword = _keyword.hide()
    fun configure(query: String) {
        _keyword.postValue(query)
    }

    val contentItems: LiveData<PagingData<NewsData>> = _keyword.switchMap {
        resultLoader(it).flowOn(Dispatchers.Default).cachedIn(this).asLiveData()
    }

    private fun resultLoader(query: String): Flow<PagingData<NewsData>> =
        loadRemoteNews(query)


    private fun loadRemoteNews(query: String): Flow<PagingData<NewsData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NaverSearchDataSource.defaultDisplay,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverSearchDataSource(query, getRemoteSearchUseCase)
            }
        ).flow
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

    private val _contentClick = MutableLiveData<String>()
    val contentClick = _contentClick.event()

    private val _selectedCategory = MutableLiveData(0)
    private val _categories = arrayOf("뉴스", "View", "이미지", "지식in", "인플루언서", "쇼핑", "지식백과")
    private val _searchCategory = MutableLiveData(
        _categories.toList()
    )
    val searchCategoryItem = _searchCategory.map { categories ->
        categories.mapIndexed { id, category ->
            id.formalize(category)
        }
    }

    private fun Int.formalize(name: String) = Result.CategoryItem(
        id = this,
        name = name,
        selected = _selectedCategory.map { it == this }
    )

    val categoryCallback = object : Result.Callback {
        override fun toggle(id: Int) {
            Timber.d("toggle id : $id")
            _selectedCategory.postValue(id)
        }
    }

    val contentCallback = object : Content.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun click(item: NewsData) {
            Timber.d("click data: $item")
            _contentClick.postValue(item.link ?: item.originallink)
        }
    }
}