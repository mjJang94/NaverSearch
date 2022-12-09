package com.mj.naversearch.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.naversearch.util.hide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ResultViewModel : ViewModel(), CoroutineScope {

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
            _keyword.postValue(null)
            _uiEvent.emit(ResultEvent.Searching(null))
        }
    }
}