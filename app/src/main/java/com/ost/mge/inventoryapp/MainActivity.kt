package com.ost.mge.inventoryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ost.mge.inventoryapp.categories.CategoriesList
import com.ost.mge.inventoryapp.categories.CategoriesView
import com.ost.mge.inventoryapp.categories.ItemsView
import com.ost.mge.inventoryapp.ui.theme.InventoryAppTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by mainViewModel.state.collectAsState()

//            InventoryAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "categories") {
                    composable("categories") {
                        CategoriesView(
                            state = state,
                            navController = navController,
                            createTestDate = mainViewModel::createTestData
                        )
                    }
                    composable("categories/{categoryId}/items") { backStackEntry ->
                        ItemsView(
                            navController = navController,
                            categoryId = backStackEntry.arguments?.getString("categoryId")!!
                        )
                    }
                }
//            }
        }
    }


}
