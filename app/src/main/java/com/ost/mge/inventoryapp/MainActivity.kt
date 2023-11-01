package com.ost.mge.inventoryapp

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
import com.ost.mge.inventoryapp.items.ItemView
import com.ost.mge.inventoryapp.items.ItemsView
import com.ost.mge.inventoryapp.ui.theme.InventoryAppTheme

val space = 16.dp
val spaceHalf = 8.dp
val spaceQuarter = 4.dp
val spaceOneAndQuarter = 20.dp
val listItemHeight = 80.dp

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.createTestData()
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
