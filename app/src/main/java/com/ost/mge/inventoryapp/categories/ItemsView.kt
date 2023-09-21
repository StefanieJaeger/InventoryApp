package com.ost.mge.inventoryapp.categories

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemsView(navController: NavController, categoryId: String) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "ITEMS for $categoryId") }, navigationIcon = {
            IconButton(onClick = navController::navigateUp) {
                Icon(Icons.Default.ArrowBack, "back")
            }
        })
    }) { padding ->
        Text(text = "Salut", Modifier.padding(padding))
    }
}