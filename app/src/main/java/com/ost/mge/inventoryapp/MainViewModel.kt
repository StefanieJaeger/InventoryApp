package com.ost.mge.inventoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class State(val categories: List<String>)

class MainViewModel : ViewModel() {
    val state = MutableStateFlow(State(emptyList()))

    fun createTestData() {
        viewModelScope.launch {
            state.emit(State(listOf("a", "b", "c")))
        }
    }
}