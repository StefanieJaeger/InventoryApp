package com.ost.mge.inventoryapp

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {
    private var _categories = mutableStateListOf<Category>()
    private var _categoriesFlow = MutableStateFlow(_categories)

    val categoriesFlow: StateFlow<List<Category>> get() = _categoriesFlow

    fun createTestData() {
        viewModelScope.launch {
            _categories.addAll(
                listOf(
                    Category(1, "Category 1", listOf(Item("1", "Item 1.1", "This is item 1.1"), Item("2", "Item 1.2", "This is item 1.2"))),
                    Category(2, "Category 2", listOf(Item("1", "Item 2.1", "This is item 2.1"))),
                    Category(3, "Category 3", listOf(Item("1", "Item 3.1", "This is item 3.1"))),
                    Category(4, "Category 4", listOf(Item("1", "Item 4.1", "This is item 4.1"))),
                    Category(5, "Category 5", listOf(Item("1", "Item 5.1", "This is item 5.1"))),
                    Category(6, "Category 6", listOf(Item("1", "Item 6.1", "This is item 6.1"))),
                    Category(7, "Category 7", listOf(Item("1", "Item 7.1", "This is item 7.1"))),
                    Category(8, "Category 8", listOf(Item("1", "Item 8.1", "This is item 8.1"))),
                )
            )
        }
    }

    fun updateItem(category: Category, item: Item) {
        val categoryIndex = _categories.indexOf(category)
        val category = _categories[categoryIndex]
        val itemIndex = category.items.indexOf(item)
        val updatedNestedList = category.items.toMutableList()

        updatedNestedList[itemIndex] = item // Replace the item in the nested list
        category.items = updatedNestedList // Update the nested list in the outer item

        // Notify the StateFlow of the change
        _categoriesFlow.value = _categories;
    }

    fun updateCategory(category: Category) {
        val categoryIndex = _categories.indexOf(category)

        _categories[categoryIndex] = category

        _categoriesFlow.value = _categories
    }

    fun removeCategory(category: Category) {
        _categories.remove(category)

        _categoriesFlow.value = _categories
    }

    fun addCategoryWithDefaultName() {
        val newId = (_categories.maxOfOrNull(Category::id) ?: 0) + 1
        _categories.add(Category(newId, "Default", emptyList()))

        _categoriesFlow.value = _categories;
    }
}