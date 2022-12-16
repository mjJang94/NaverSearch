package com.mj.naversearch.ui.result.books

import androidx.lifecycle.*
import androidx.paging.*
import com.mj.domain.model.books.BookData
import com.mj.domain.usecase.GetRemoteBookUseCase
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
import com.mj.naversearch.ui.result.books.BooksAdapter as Books

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getRemoteBooksUseCase: GetRemoteBookUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    sealed class BooksEvent {
        object LoadSuccess : BooksEvent()
        object ItemsEmpty : BooksEvent()
        data class OpenLink(val link: String) : BooksEvent()
        data class LoadError(val error: Throwable) : BooksEvent()
    }

    private val _uiEvent = MutableSharedFlow<BooksEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun eventTrigger(event: BooksEvent) {
        launch {
            _uiEvent.emit(event)
        }
    }

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
            pageSize = BooksDataSource.defaultDisplay,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            BooksDataSource(query, getRemoteBooksUseCase)
        }
    ).flow

    val callback = object : Books.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun onLoadState(size: Int, state: CombinedLoadStates) {
            when (val s = state.refresh) {
                is LoadState.NotLoading -> {
                    val event = when (state.append.endOfPaginationReached && size < 1) {
                        true -> BooksEvent.ItemsEmpty
                        else -> BooksEvent.LoadSuccess
                    }
                    eventTrigger(event)
                }
                is LoadState.Error -> eventTrigger(BooksEvent.LoadError(s.error))
                else -> {}
            }
        }

        override fun click(item: BookData) {
            eventTrigger(BooksEvent.OpenLink(item.link))
        }
    }

    private class BooksDataSource(
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
                val response = searchUseCase.searchBooks(query, loadSize, start)
                val nextKey = if (response.items.isEmpty() || start >= (response.total / loadSize)) null else start + 1
                val prevKey = if (start == defaultStart) null else start - defaultDisplay
                LoadResult.Page(response.items, prevKey, nextKey)
            } catch (exception: Exception) {
                LoadResult.Error(exception)
            }
        }

        companion object {
            const val loadSize = 10
            const val defaultStart = 1
            const val defaultDisplay = 10
        }
    }
}