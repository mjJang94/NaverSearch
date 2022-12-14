package com.mj.naversearch.ui.result.books

import androidx.lifecycle.*
import androidx.paging.*
import com.mj.domain.model.books.BookData
import com.mj.domain.usecase.GetRemoteBookUseCase
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
import com.mj.naversearch.ui.result.books.BooksAdapter as Books

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getRemoteBooksUseCase: GetRemoteBookUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val _keyword = MutableLiveData<String>()
    fun configure(query: String?) {
        _keyword.postValue(query)
    }

    val booksItem: LiveData<PagingData<BookData>> = _keyword.switchMap {
        booksLoader(it).flowOn(Dispatchers.IO).cachedIn(this).asLiveData()
    }

    private fun booksLoader(query: String): Flow<PagingData<BookData>> = loadRemoteBooks(query)

    private fun loadRemoteBooks(query: String): Flow<PagingData<BookData>> = Pager(
        config = PagingConfig(
            pageSize = NaverBooksDataSource.defaultDisplay,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            NaverBooksDataSource(query, getRemoteBooksUseCase)
        }
    ).flow

    private val _contentClick = MutableLiveData<String>()
    val contentClick = _contentClick.event()

    private val _isItemsEmpty = MutableLiveData<Boolean>()
    val isItemsEmpty = _isItemsEmpty.hide()

    private val _errorEvent = MutableLiveData<Throwable>()
    val errorEvent = _errorEvent.event()

    val callback = object : Books.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun onLoadState(size: Int, state: CombinedLoadStates) {
            when (val s = state.refresh) {
                is LoadState.NotLoading -> _isItemsEmpty.postValue(state.append.endOfPaginationReached && size < 1)
                is LoadState.Error -> _errorEvent.postValue(s.error)
                else -> {}
            }
        }
        override fun click(item: BookData) {
            _contentClick.postValue(item.link)
        }
    }

    private class NaverBooksDataSource(
        private val query: String,
        private val searchUseCase: GetRemoteBookUseCase
    ) : PagingSource<Int, BookData>() {
        override fun getRefreshKey(state: PagingState<Int, BookData>): Int? = state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookData> {
            val start = params.key ?: defaultStart

            return try {
                val response = searchUseCase.searchBooks(query, 30, start)
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