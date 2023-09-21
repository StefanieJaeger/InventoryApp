package com.ost.mge.inventoryapp.data

import com.ost.mge.inventoryapp.data.Category

class DataSource {
    fun loadCategories(): List<Category> {
//        return listOf<Category>(Category(R.string.categoryName1),Category(R.string.categoryName2), Category(R.string.categoryName3))
        return listOf<Category>(
            Category("Category 1"),
            Category("Category 2"), Category("Category 3")
        )
    }

    fun loadItemsForCategoryName(categorName: String): List<Item> {
        return listOf<Item>(Item("Item 1", "This is the first item for $categorName"), Item("Item 2", "This is the second item for $categorName"))
    }
}