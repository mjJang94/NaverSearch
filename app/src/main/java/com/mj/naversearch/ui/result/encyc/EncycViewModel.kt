package com.mj.naversearch.ui.result.encyc

import androidx.lifecycle.*
import androidx.paging.*
import com.mj.domain.model.encyc.EncycData
import com.mj.domain.usecase.GetRemoteEncycUseCase
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
import com.mj.naversearch.ui.result.encyc.EncycAdapter as Encyc

@HiltViewModel
class EncycViewModel @Inject constructor(
    private val getRemoteEncycUseCase: GetRemoteEncycUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    sealed class EncycEvent {
        object LoadSuccess : EncycEvent()
        object Loading : EncycEvent()
        object ItemsEmpty : EncycEvent()
        data class OpenLink(val link: String) : EncycEvent()
        data class LoadError(val error: Throwable) : EncycEvent()
    }

    private val _uiEvent = MutableSharedFlow<EncycEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun eventTrigger(event: EncycEvent) {
        launch {
            _uiEvent.emit(event)
        }
    }

    private val _keyword = MutableLiveData<String>()
    fun configure(query: String?) {
        _keyword.postValue(query)
    }

    val encycItems: LiveData<PagingData<EncycData>> = _keyword.switchMap {
        encycLoader(it).flowOn(Dispatchers.IO).cachedIn(this).asLiveData()
    }

    private fun encycLoader(query: String): Flow<PagingData<EncycData>> =
        loadRemoteNews(query)


    private fun loadRemoteNews(query: String): Flow<PagingData<EncycData>> = Pager(
        config = PagingConfig(
            pageSize = EncycDataSource.defaultDisplay,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            EncycDataSource(query, getRemoteEncycUseCase)
        }
    ).flow

    val callback = object : Encyc.Callback {
        override val coroutineScope: CoroutineScope = viewModelScope
        override fun onLoadState(size: Int, state: CombinedLoadStates) {
            when (val s = state.refresh) {
                is LoadState.NotLoading -> {
                    val event = when (state.append.endOfPaginationReached && size < 1) {
                        true -> EncycEvent.ItemsEmpty
                        else -> EncycEvent.LoadSuccess
                    }
                    eventTrigger(event)
                }
                is LoadState.Error -> eventTrigger(EncycEvent.LoadError(s.error))
                is LoadState.Loading -> eventTrigger(EncycEvent.Loading)
            }
        }

        override fun click(item: EncycData) {
            eventTrigger(EncycEvent.OpenLink(item.link))
        }
    }

    private class EncycDataSource(
        private val query: String,
        private val searchUseCase: GetRemoteEncycUseCase
    ) : PagingSource<Int, EncycData>() {
        override fun getRefreshKey(state: PagingState<Int, EncycData>): Int? = state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EncycData> {
            val start = params.key ?: defaultStart

            return try {
                val response = searchUseCase.searchEncyc(query, loadSize, start)
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