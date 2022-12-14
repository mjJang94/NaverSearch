package com.mj.naversearch.ui.result.news

import androidx.lifecycle.*
import androidx.paging.*
import com.mj.domain.model.news.NewsData
import com.mj.domain.usecase.GetRemoteNewsUseCase
import com.mj.naversearch.util.event
import com.mj.naversearch.util.hide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import com.mj.naversearch.ui.result.news.NewsAdapter as News

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getRemoteNewsUseCase: GetRemoteNewsUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

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
            pageSize = NaverNewsDataSource.defaultDisplay,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            NaverNewsDataSource(query, getRemoteNewsUseCase)
        }
    ).flow

    private val _contentClick = MutableLiveData<String>()
    val contentClick = _contentClick.event()

    private val _isItemsEmpty = MutableLiveData<Boolean>()
    val isItemsEmpty = _isItemsEmpty.hide()

    private val _errorEvent = MutableLiveData<Throwable>()
    val errorEvent = _errorEvent.event()

    val callback = object : News.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun onLoadState(size: Int, state: CombinedLoadStates) {
            when (val s = state.refresh) {
                is LoadState.NotLoading -> _isItemsEmpty.postValue(state.append.endOfPaginationReached && size < 1)
                is LoadState.Error -> _errorEvent.postValue(s.error)
                else -> {}
            }
        }
        override fun click(item: NewsData) {
            _contentClick.postValue(item.link ?: item.originallink)
        }
    }

    private class NaverNewsDataSource(
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
                val response = searchUseCase.searchNews(query, 30, start)
                val nextKey = if (response.isEmpty()) null else start + 1
                val prevKey = if (start == defaultStart) null else start - defaultDisplay
                LoadResult.Page(response, prevKey, nextKey)
            } catch (exception: Exception) {
                LoadResult.Error(exception)
            }
        }

        companion object {
            const val defaultStart = 1
            const val defaultDisplay = 10
        }
    }
}