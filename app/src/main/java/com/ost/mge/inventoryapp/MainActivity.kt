package com.ost.mge.inventoryapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ost.mge.inventoryapp.categories.CategoriesView
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import com.ost.mge.inventoryapp.items.ItemView
import com.ost.mge.inventoryapp.items.ItemsView
import com.ost.mge.inventoryapp.ui.theme.InventoryAppTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

val space = 16.dp
val spaceHalf = 8.dp
val spaceQuarter = 4.dp
val spaceOneAndQuarter = 20.dp
val listItemHeight = 80.dp

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private fun saveData(context: Context) {
        try {
            val categoryFileStream = context.openFileOutput("categories", Context.MODE_PRIVATE)
            val categoryOutputStream = ObjectOutputStream(categoryFileStream)
            val categories = mainViewModel.categoriesFlow.value.toList()
            categoryOutputStream.writeObject(Json.encodeToString(categories))
            categoryOutputStream.close()

            val itemFileStream = context.openFileOutput("items", Context.MODE_PRIVATE)
            val itemOutputStream = ObjectOutputStream(itemFileStream)
            val items = mainViewModel.itemsFlow.value.toList()
            itemOutputStream.writeObject(Json.encodeToString(items))
            itemOutputStream.close()
        } catch (exception: Exception) {
            System.out.println(String.format("could not save the data %s", exception.message))
        }
    }

    private fun loadData(context: Context) {
        try {
            val categoryFileStream = context.openFileInput("categories")
            val categoryInputStream = ObjectInputStream(categoryFileStream)
            val categories = Json.decodeFromString<List<Category>>(categoryInputStream.readObject() as String);

            val itemFileStream = context.openFileInput("items")
            val itemInputStream = ObjectInputStream(itemFileStream)
            val items = Json.decodeFromString<List<Item>>(itemInputStream.readObject() as String);
            mainViewModel.init(categories, items)
            categoryInputStream.close()
            itemInputStream.close()
        } catch (exception: Exception) {
            // nothing to do because there is no active data.
        }

    }

    override fun onStop() {
        super.onStop();
        saveData(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData(applicationContext)
        setContent {
            val categories by mainViewModel.categoriesFlow.collectAsState()
            val items by mainViewModel.itemsFlow.collectAsState()
            InventoryAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "categories") {
                    composable("categories") {
                        CategoriesView(
                            categories = categories,
                            navController = navController,
                            onEditCategory = mainViewModel::updateCategory,
                            onDeleteCategory = mainViewModel::removeCategory,
                            onAddCategory = mainViewModel::addCategoryWithDefaultName
                        )
                    }
                    composable("categories/{categoryId}/items") { backStackEntry ->
                        val categoryId =
                            backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
                        val category = categories.first { it.id == categoryId }
                        val itemsForCategory = items.filter { it.categoryId == categoryId }
                        ItemsView(
                            navController = navController,
                            category = category,
                            items = itemsForCategory,
                            onDeleteItem = mainViewModel::removeItem,
                            onAddItem = mainViewModel::addItemWithDefaultName
                        )
                    }
                    composable("categories/{categoryId}/items/{itemId}") { backStackEntry ->
                        val categoryId =
                            backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
                        val itemId =
                            backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
                        val item = items.first { it.id == itemId && it.categoryId == categoryId }

                        ItemView(navController = navController, item) {
                            mainViewModel.updateItem(
                                it
                            )
                        }
                    }
                }
            }
        }
    }
}
