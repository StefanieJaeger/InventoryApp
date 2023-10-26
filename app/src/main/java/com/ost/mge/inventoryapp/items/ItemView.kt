package com.ost.mge.inventoryapp.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemView(
    navController: NavController,
    item: Item,
    onUpdateItem: (item: Item) -> Unit
) {
    var itemNameField by remember { mutableStateOf(item.name) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item.name) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp)
                    {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        }
    ) /*{ padding ->*/
//        SelectionContainer {
//            Text(text = "This is item ${item.name}", Modifier.padding(padding))
//        }
    { innerpadding ->
        Box {
            TextField(
                value = itemNameField,
                onValueChange = {
                    itemNameField = it
                    item.name = itemNameField
                    onUpdateItem(item)
                },
                Modifier.padding(innerpadding)
            )
            if (item.name.isEmpty()) {
                Text(text = "Placeholder")
            }
        }
    }
}