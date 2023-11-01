package com.ost.mge.inventoryapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _categories = mutableStateListOf<Category>()
    private var _categoriesFlow = MutableStateFlow(_categories)

    val categoriesFlow: StateFlow<List<Category>> get() = _categoriesFlow

    fun createTestData() {
        viewModelScope.launch {
            _categories.addAll(
                listOf(
                    Category(
                        1,
                        "Category 1",
                        listOf(
                            Item(1, "Item 1.1", "This is item 1.1"),
                            Item(2, "Item 1.2", "This is item 1.2")
                        )
                    ),
                    Category(2, "Category 2", listOf(Item(1, "Item 2.1", "This is item 2.1"))),
                    Category(3, "Category 3", listOf(Item(1, "Item 3.1", "This is item 3.1"))),
                    Category(4, "Category 4", listOf(Item(1, "Item 4.1", "This is item 4.1"))),
                    Category(5, "Category 5", listOf(Item(1, "Item 5.1", "This is item 5.1"))),
                    Category(6, "Category 6", listOf(Item(1, "Item 6.1", "This is item 6.1"))),
                    Category(7, "Category 7", listOf(Item(1, "Item 7.1", "This is item 7.1"))),
                    Category(8, "Category 8", listOf(Item(1, "Item 8.1", "This is item 8.1"))),
                )
            )
        }
    }

    fun updateItem(categoryId: Int, item: Item) {
        val category = _categories.first{it.id == categoryId}

        val updatedNestedList = category.items.toMutableList()
        updatedNestedList.replaceAll{if (it.id == item.id) item else it}

        category.items = updatedNestedList

        updateCategory(category)
    }

    fun removeItem(categoryId: Int, itemId: Int) {
        val category = _categories.first{it.id == categoryId}

        val updatedNestedList = category.items.toMutableList()
        updatedNestedList.removeIf{it.id == itemId}

        category.items = updatedNestedList

        updateCategory(category)
    }

    fun addItemWithDefaultName(categoryId: Int) {
        val category = _categories.first {it.id == categoryId}
        val newId = (category.items.maxOfOrNull(Item::id) ?: 0) + 1

        val updatedNestedList = category.items.toMutableList()
        updatedNestedList.add(Item(newId, "Default", "Default"))

        category.items = updatedNestedList

        updateCategory(category)
    }

    fun updateCategory(category: Category) {
        _categories.replaceAll {if (it.id == category.id) category else it}

        _categoriesFlow.value = _categories
    }

    fun removeCategory(categoryId: Int) {
        _categories.removeIf{it.id == categoryId}

        _categoriesFlow.value = _categories
    }

    fun addCategoryWithDefaultName() {
        val newId = (_categories.maxOfOrNull(Category::id) ?: 0) + 1
        _categories.add(Category(newId, "Default", emptyList()))

        _categoriesFlow.value = _categories
    }
}