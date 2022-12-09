package com.mj.naversearch.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.naversearch.ui.main.MainViewModel.MainEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    sealed class MainEvent {
        object Searching : MainEvent()
    }

    private val _uiEvent = MutableSharedFlow<MainEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    fun searching() {
        launch {
            _uiEvent.emit(Searching)
        }
    }
}