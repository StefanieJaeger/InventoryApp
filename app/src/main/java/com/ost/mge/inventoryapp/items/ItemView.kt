package com.ost.mge.inventoryapp.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
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
import com.ost.mge.inventoryapp.data.Item
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemView(navController: NavController, item: Item) {
    var itemName by remember { mutableStateOf(item.name) }
//    var itemDescription by remember { mutableStateOf(item.description) }
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
//                label = { Text(text= "Itemname")},
                value = itemName,
                onValueChange = { itemName = it },
                Modifier.padding(innerpadding)
            )
            if (item.name.isEmpty()) {
                Text(text = "Placeholder")
            }
        }
    }
}