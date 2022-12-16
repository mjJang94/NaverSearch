package com.mj.naversearch.ui.result.news

import androidx.lifecycle.*
import androidx.paging.*
import com.mj.domain.model.news.NewsData
import com.mj.domain.usecase.GetRemoteNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.mj.naversearch.ui.result.news.NewsAdapter as News

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getRemoteNewsUseCase: GetRemoteNewsUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    sealed class NewsEvent {
        object LoadSuccess : NewsEvent()
        object ItemsEmpty : NewsEvent()
        data class OpenLink(val link: String) : NewsEvent()
        data class LoadError(val error: Throwable) : NewsEvent()
    }

    private val _uiEvent = MutableSharedFlow<NewsEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun eventTrigger(event: NewsEvent) {
        launch {
            _uiEvent.emit(event)
        }
    }

    private val _keyword = MutableLiveData<String>()
    fun configure(query: String?) {
        _keyword.postValue(query)
    }

    val newsItems: LiveData<PagingData<NewsData>> = _keyword.switchMap {
        newsLoader(it).flowOn(Dispatchers.IO).cachedIn(this).asLiveData()
    }

    private fun newsLoader(query: String): Flow<PagingData<NewsData>> =
        loadRemoteNews(query)


    private fun loadRemoteNews(query: String): Flow<PagingData<NewsData>> = Pager(
        config = PagingConfig(
            pageSize = NewsDataSource.defaultDisplay,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            NewsDataSource(query, getRemoteNewsUseCase)
        }
    ).flow

    val callback = object : News.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun onLoadState(size: Int, state: CombinedLoadStates) {
            when (val s = state.refresh) {
                is LoadState.NotLoading -> {
                    val event = when (state.append.endOfPaginationReached && size < 1) {
                        true -> NewsEvent.ItemsEmpty
                        else -> NewsEvent.LoadSuccess
                    }
                    eventTrigger(event)
                }
                is LoadState.Error -> eventTrigger(NewsEvent.LoadError(s.error))
                else -> {}
            }
        }

        override fun click(item: NewsData) {
            eventTrigger(NewsEvent.OpenLink(item.link ?: item.originallink))
        }
    }

    private class NewsDataSource(
        private val query: String,
        private val searchUseCase: GetRemoteNewsUseCase
    ) : PagingSource<Int, NewsData>() {
        override fun getRefreshKey(state: PagingState<Int, NewsData>): Int? = state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsData> {
            val start = params.key ?: defaultStart

            return try {
                val response = searchUseCase.searchNews(query, loadSize, start)
                val nextKey = if (response.items.isEmpty() || start >= (response.total / loadSize)) null else start + 1
                val prevKey = if (start == defaultStart) null else start - defaultDisplay
                LoadResult.Page(response.items, prevKey, nextKey)
            } catch (exception: Exception) {
                LoadResult.Error(exception)
            }
        }

        companion object {
            const val loadSize = 20
            const val defaultStart = 1
            const val defaultDisplay = 10
        }
    }
}