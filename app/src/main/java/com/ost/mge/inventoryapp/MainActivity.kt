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

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.createTestData()
        setContent {
            val categories by mainViewModel.categoriesFlow.collectAsState()
//            InventoryAppTheme {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "categories") {
                composable("categories") {
                    CategoriesView(
                        categories = categories,
                        navController = navController,
                        onEditCategory = {category -> mainViewModel.updateCategory(category)},
                        onDeleteCategory = {category -> mainViewModel.removeCategory(category)})
                }
                composable("categories/{categoryId}/items") { backStackEntry ->
                    val category =
                        categories.find { c -> c.id == backStackEntry.arguments?.getString("categoryId") }
                    if (category === null) {
                        throw Exception("category not found")
                    }
                    ItemsView(navController = navController, category = category)
                }
                composable("categories/{categoryId}/items/{itemId}") { backStackEntry ->
                    val category =
                        categories.find { c -> c.id == backStackEntry.arguments?.getString("categoryId") }
                    if (category === null) {
                        throw Exception("category not found")
                    }
                    val item =
                        category.items.find { i -> i.id == backStackEntry.arguments?.getString("itemId") }
                    if (item === null) {
                        throw Exception("item not found")
                    }
                    ItemView(navController = navController, item) {
                        mainViewModel.updateItem(
                            category,
                            it
                        )
                    }
                }
            }
//            }
        }
    }
}
