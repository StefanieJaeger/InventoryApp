package com.ost.mge.inventoryapp.items

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
import androidx.navigation.NavController
import com.ost.mge.inventoryapp.data.Category

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemsView(navController: NavController, category: Category) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "ITEMS FOR ${category.name}") }, navigationIcon = {
            IconButton(onClick = navController::navigateUp) {
                Icon(Icons.Default.ArrowBack, "back")
            }
        })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(category.items) { item ->
                    Button(onClick = { navController.navigate("categories/${category.id}/items/${item.id}") }) {
                        Text(item.name)
                    }
                }
            }
        }
    }
}