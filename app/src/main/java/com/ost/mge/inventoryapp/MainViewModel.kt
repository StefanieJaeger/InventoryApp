package com.ost.mge.inventoryapp

import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
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

    fun init(categories: List<Category>, items: List<Item>) {
        viewModelScope.launch {
            _categories.clear()
            _items.clear()
            _categories.addAll(
                categories.distinctBy { it.id }
            )
            _items.addAll(
                items.distinctBy { it.id }
            )
        }
    }

    fun updateItem(item: Item) {
        _items.replaceAll { if (it.id == item.id) item else it }

        _itemsFlow.value = _items
    }

    fun removeItem(itemId: Int) {
        _items.removeIf { it.id == itemId }

        _itemsFlow.value = _items
    }

    fun addItemWithDefaultName(categoryId: Int) {
        val newId = (_items.maxOfOrNull(Item::id) ?: 0) + 1
        _items.add(Item(newId, "Default", "Default", "".toUri(), categoryId))

        _itemsFlow.value = _items
    }

    fun updateCategory(category: Category) {
        _categories.replaceAll { if (it.id == category.id) category else it }

        _categoriesFlow.value = _categories
    }

    fun removeCategory(categoryId: Int) {
        _categories.removeIf { it.id == categoryId }

        _categoriesFlow.value = _categories
    }

    fun addCategoryWithDefaultName() {
        val newId = (_categories.maxOfOrNull(Category::id) ?: 0) + 1
        _categories.add(Category(newId, "Default"))

        _categoriesFlow.value = _categories
    }
}