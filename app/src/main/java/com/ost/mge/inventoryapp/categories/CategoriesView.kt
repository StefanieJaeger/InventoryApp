package com.ost.mge.inventoryapp.categories

import SearchView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.DraggableItem
import com.ost.mge.inventoryapp.InlineTextField
import com.ost.mge.inventoryapp.data.Category

// draggable/swipe-able item: https://github.com/cp-radhika-s/swipe-to-action-blog-demo/
enum class DragAnchors {
    Start,
    End,
}

val space = 16.dp
val spaceHalf = 8.dp
val listItemHeight = 80.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CategoriesView(
    categories: List<Category>,
    navController: NavHostController,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onAddCategory: () -> Unit,
) {
    val searchText = remember { mutableStateOf("") }
    var filteredCategories = categories
    if (searchText.value.isNotEmpty()) {
        filteredCategories =
            categories.filter { it.name.contains(searchText.value, ignoreCase = true) }
    }
    val categoryIdBeingEdited = remember { mutableStateOf(null as Int?) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Categories") })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddCategory,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Filled.Add, "Add a category")
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { categoryIdBeingEdited.value = null }),
                )
            {
                SearchView(searchText) { text ->
                    categories
                        .map(Category::name)
                        .filter { it.contains(text, ignoreCase = true) }
                }
                LazyColumn(
                    contentPadding = PaddingValues(spaceHalf),
                    verticalArrangement = Arrangement.spacedBy(spaceHalf)
                ) {
                    items(filteredCategories) { category ->
                        DraggableCategoryListItem(
                            category,
                            onCategoryClick = { navController.navigate("categories/${category.id}/items") },
                            onEditCategory,
                            onDeleteCategory,
                            categoryIdBeingEdited,
                        )
                    }
                }
            }
    }
}

@Composable
fun DraggableCategoryListItem(
    category: Category,
    onCategoryClick: (Category) -> Unit,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    categoryIdBeingEdited: MutableState<Int?>
) {
    val editMode = categoryIdBeingEdited.value == category.id
    Row (modifier = Modifier
        .background(color = Color.Transparent)
        .clip(RoundedCornerShape(spaceHalf))
    ) {
        DraggableItem(
            onEdit = { categoryIdBeingEdited.value = category.id },
            onDelete = { onDeleteCategory(category) },
            !editMode
        ) {
            Box(
                modifier = Modifier
                    .run { if (!editMode) clickable(onClick = { onCategoryClick(category) }) else this }
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )
                    .padding(space, 0.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (editMode) {
                    InlineTextField(
                        category.name,
                        onDone = {
                            category.name = it
                            onEditCategory(category)
                            categoryIdBeingEdited.value = null
                        })
                } else {
                    Text(category.name)
                }
            }
        }
    }
}
