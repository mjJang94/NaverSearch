package com.mj.naversearch.ui.result.news

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
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.mj.naversearch.ui.result.news.NewsAdapter as Content

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getRemoteSearchUseCase: GetRemoteSearchUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val _keyword = MutableLiveData<String>()
    val keyword = _keyword.hide()
    fun configure(query: String) {
        _keyword.postValue(query)
    }

    val newsItems: LiveData<PagingData<NewsData>> = _keyword.switchMap {
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

    private val _contentClick = MutableLiveData<String>()
    val contentClick = _contentClick.event()

    val contentCallback = object : Content.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun click(item: NewsData) {
            Timber.d("click data: $item")
            _contentClick.postValue(item.link ?: item.originallink)
        }
    }
}