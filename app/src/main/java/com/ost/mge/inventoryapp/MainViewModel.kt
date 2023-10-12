package com.ost.mge.inventoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class State(val categories: List<Category>)

class MainViewModel : ViewModel() {
    val state = MutableStateFlow(State(emptyList()))

    fun createTestData() {
        viewModelScope.launch {
            state.emit(State(listOf(
                Category("1", "Category 1", listOf(Item("1", "Item 1.1", "This is item 1.1"), Item("2", "Item 1.2", "This is item 1.2"))),
                Category("2", "Category 2", listOf(Item("1", "Item 2.1", "This is item 2.1"))),
                )))
        }
    }
}