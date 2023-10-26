package com.ost.mge.inventoryapp.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.data.Category

@Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun CategoriesView(
    categories: List<Category>,
    navController: NavHostController
    ) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "CATEGORIES") })
    })  { padding ->
            Box(modifier = Modifier.padding(padding)) {
                LazyColumn {
                    items(categories) { category ->
                        Button(onClick = { navController.navigate("categories/${category.id}/items") }) {
                            Text(category.name)
                        }
                    }
                }
            }
        }
    }