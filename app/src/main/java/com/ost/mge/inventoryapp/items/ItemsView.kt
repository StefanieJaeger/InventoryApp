package com.ost.mge.inventoryapp.items

import SearchView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.DraggableItem
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.Item
import com.ost.mge.inventoryapp.space
import com.ost.mge.inventoryapp.spaceHalf

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemsView(
    category: Category,
    navController: NavHostController,
    onDeleteItem: (Int, Int) -> Unit,
    onAddItem: (Int) -> Unit,
) {
    val searchText = remember { mutableStateOf("") }
    var filteredCategories = category.items
    if (searchText.value.isNotEmpty()) {
        filteredCategories =
            category.items.filter { it.name.contains(searchText.value, ignoreCase = true) }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Items for ${category.name}") }, navigationIcon = {
            IconButton(onClick = navController::navigateUp) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {onAddItem(category.id)}, containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, "Add an item")
        }
    }) { padding ->
        Column(
            Modifier.padding(padding)
        ) {
            SearchView(searchText) { text ->
                category.items.map(Item::name).filter { it.contains(text, ignoreCase = true) }
            }
            LazyColumn(
                contentPadding = PaddingValues(spaceHalf),
                verticalArrangement = Arrangement.spacedBy(spaceHalf)
            ) {
                items(filteredCategories) { item ->
                    DraggableItemListItem(
                        categoryId = category.id,
                        item = item,
                        onItemClick = { navController.navigate("categories/${category.id}/items/${item.id}") },
                        onDeleteItem = onDeleteItem,
                    )
                }
            }
        }
    }
}

@Composable
fun DraggableItemListItem(
    categoryId: Int,
    item: Item,
    onDeleteItem: (Int, Int) -> Unit,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(color = Color.Transparent)
            .clip(RoundedCornerShape(spaceHalf))
    ) {
        DraggableItem(
            onDelete = { onDeleteItem(categoryId, item.id) },
            dragEnabled = true
        ) {
            Box(
                modifier = Modifier
                    .clickable(onClick = onItemClick)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                    .padding(space, 0.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                    Text(item.name)
            }
        }
    }
}
