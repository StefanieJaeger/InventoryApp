package com.ost.mge.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ost.mge.inventoryapp.categories.CategoriesView
import com.ost.mge.inventoryapp.items.ItemView
import com.ost.mge.inventoryapp.items.ItemsView
import com.ost.mge.inventoryapp.ui.theme.InventoryAppTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.createTestData()
        setContent {
            val categories by mainViewModel.categoriesFlow.collectAsState()
            InventoryAppTheme {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "categories") {
                composable("categories") {
                    CategoriesView(
                        categories = categories,
                        navController = navController,
                        onEditCategory = mainViewModel::updateCategory,
                        onDeleteCategory = mainViewModel::removeCategory,
                        onAddCategory = mainViewModel::addCategoryWithDefaultName)
                }
                composable("categories/{categoryId}/items") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
                    val category =
                        categories.first { c -> c.id ==  categoryId}
                    ItemsView(navController = navController, category = category)
                }
                composable("categories/{categoryId}/items/{itemId}") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
                    val itemId = backStackEntry.arguments?.getString("itemId")
                    val category =
                        categories.first { c -> c.id ==  categoryId}
                    val item =
                        category.items.first { i -> i.id == itemId }
                    ItemView(navController = navController, item) {
                        mainViewModel.updateItem(
                            category,
                            it
                        )
                    }
                }
            }
            }
        }
    }
}
