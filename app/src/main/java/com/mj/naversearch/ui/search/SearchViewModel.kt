package com.mj.naversearch.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.naversearch.ui.search.SearchViewModel.SearchEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    sealed class SearchEvent {
        data class Search(val query: String) : SearchEvent()
        object Dismiss : SearchEvent()
    }

    private val _uiEvent = MutableSharedFlow<SearchEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val keyword: MutableLiveData<String> = MutableLiveData()

    fun configure(query: String?) {
        keyword.postValue(query)
    }

    fun search() {
        launch {
            val query = keyword.value?.trim() ?: return@launch
            _uiEvent.emit(Search(query))
        }
    }

    fun back() {
        launch {
            _uiEvent.emit(Dismiss)
        }
    }
}