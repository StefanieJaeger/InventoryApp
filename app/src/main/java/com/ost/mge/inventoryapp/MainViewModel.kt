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
    private var _items = mutableStateListOf<Item>()
    private var _categoriesFlow = MutableStateFlow(_categories)
    private var _itemsFlow = MutableStateFlow(_items)

    val categoriesFlow: StateFlow<List<Category>> get() = _categoriesFlow
    val itemsFlow: StateFlow<List<Item>> get() = _itemsFlow

    fun createTestData() {
        viewModelScope.launch {
            _categories.addAll(
                listOf(
                    Category(1, "Category 1"),
                    Category(2, "Category 2"),
                    Category(3, "Category 3"),
                    Category(4, "Category 4"),
                    Category(5, "Category 5"),
                    Category(6, "Category 6"),
                    Category(7, "Category 7"),
                    Category(8, "Category 8"),
                )
            )
            _items.addAll(
                listOf(
                    Item(1, "Item 1.1", "This is item 1.1", 1),
                    Item(2, "Item 1.2", "This is item 1.2", 1),
                    Item(3, "Item 2.1", "This is item 2.1", 2),
                    Item(4, "Item 3.1", "This is item 3.1", 3),
                    Item(5, "Item 4.1", "This is item 4.1", 4),
                    Item(6, "Item 5.1", "This is item 5.1", 5),
                    Item(7, "Item 6.1", "This is item 6.1", 6),
                    Item(8, "Item 7.1", "This is item 7.1", 7),
                    Item(9, "Item 8.1", "This is item 8.1", 8),
                )
            )
        }
    }

    fun updateItem(item: Item) {
        _items.replaceAll {if (it.id == item.id) item else it}

        _itemsFlow.value = _items
    }

    fun removeItem(itemId: Int) {
        _items.removeIf{it.id == itemId}

        _itemsFlow.value = _items
    }

    fun addItemWithDefaultName(categoryId: Int) {
        val newId = (_items.maxOfOrNull(Item::id) ?: 0) + 1
        _items.add(Item(newId, "Default", "Default", categoryId))

        _itemsFlow.value = _items
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
        _categories.add(Category(newId, "Default"))

        _categoriesFlow.value = _categories
    }
}