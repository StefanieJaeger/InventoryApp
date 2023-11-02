package com.ost.mge.inventoryapp

import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var categories = mutableStateListOf<Category>()
    var _categoriesFlow = MutableStateFlow(categories)

    val categoriesFlow: StateFlow<List<Category>> get() = _categoriesFlow

    fun createTestData() {
        viewModelScope.launch {
            categories.addAll(
                listOf(
                    Category("1", "Category 1", listOf(Item("1", "Item 1.1", "This is item 1.1", "".toUri()), Item("2", "Item 1.2", "This is item 1.2", "".toUri()))),
                    Category("3", "Category 3", listOf(Item("1", "Item 3.1", "This is item 3.1", "".toUri()))),
                    Category("2", "Category 2", listOf(Item("1", "Item 2.1", "This is item 2.1", "".toUri()))),
                    Category("4", "Category 4", listOf(Item("1", "Item 4.1", "This is item 4.1", "".toUri()))),
                    Category("5", "Category 5", listOf(Item("1", "Item 5.1", "This is item 5.1", "".toUri()))),
                    Category("6", "Category 6", listOf(Item("1", "Item 6.1", "This is item 6.1", "".toUri()))),
                    Category("7", "Category 7", listOf(Item("1", "Item 7.1", "This is item 7.1", "".toUri()))),
                    Category("8", "Category 8", listOf(Item("1", "Item 8.1", "This is item 8.1", "".toUri()))),
                )
            )
        }
    }

    fun updateItem(category: Category, item: Item) {
        val categoryIndex = categories.indexOf(category);
        val category = categories[categoryIndex]
        val itemIndex = category.items.indexOf(item);
        val updatedNestedList = category.items.toMutableList()

        updatedNestedList[itemIndex] = item // Replace the item in the nested list
        category.items = updatedNestedList // Update the nested list in the outer item

        // Notify the StateFlow of the change
        _categoriesFlow.value = categories;
    }

}