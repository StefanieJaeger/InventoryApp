package com.ost.mge.inventoryapp.categories

import SearchView
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.DeleteAction
import com.ost.mge.inventoryapp.EditAction
import com.ost.mge.inventoryapp.InlineTextField
import com.ost.mge.inventoryapp.data.Category
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// draggable/swipe-able item: https://github.com/cp-radhika-s/swipe-to-action-blog-demo/
enum class DragAnchors {
    Start,
    End,
}

@Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun CategoriesView(
    categories: List<Category>,
    navController: NavHostController,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit
    ) {
    val searchText = remember { mutableStateOf("") }
    var filteredCategories = categories
    if(searchText.value.isNotEmpty()) {
        filteredCategories = categories.filter {it.name.contains(searchText.value, ignoreCase = true)}
    }
    val categoryIdBeingEdited = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Categories") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon(Icons.Filled.Add, "Add a category")
            }
        }
        )  { padding ->
        Column(Modifier.padding(padding).clickable { categoryIdBeingEdited.value = "" })
        {
            SearchView(searchText) { text ->
                categories.filter {
                    it.name.contains(
                        text,
                        ignoreCase = true
                    )
                }.map { it.name }
            }
            LazyColumn {
                items(filteredCategories) { category ->
                    DraggableCategoryListItem(
                        category,
                        onCategoryClick = {navController.navigate("categories/${category.id}/items")},
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
    categoryIdBeingEdited: MutableState<String>
    ) {
    val editMode = categoryIdBeingEdited.value === category.id
    Row {
        DraggableItem (
            onEdit = {categoryIdBeingEdited.value = category.id},
            onDelete = {onDeleteCategory(category)},
            !editMode) {
            Box(
                Modifier
                    .clickable(onClick = { if (!editMode) onCategoryClick(category) })
                    // todo: use our themed color
                    .background(Color.Green)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // todo: style list item nicely
                if(editMode) {
                    InlineTextField(category.name, onDone = {category.name = it; onEditCategory(category); categoryIdBeingEdited.value = ""})
                } else {
                    Text(category.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableItem(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    dragEnabled: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val defaultActionSize = 80.dp
    val actionsSizePx = with(density) { (defaultActionSize * 2).toPx() }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.End at actionsSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val editAction = {
        coroutineScope.launch {
            resetAnchoredDraggableState(draggableState)
        }
        onEdit()
    }

    val deleteAction = {
        coroutineScope.launch {
            resetAnchoredDraggableState(draggableState)
        }
        onDelete()
    }

    // todo: style list item, then decide how to style these buttons
    // todo: also, move this to own file
    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(100.dp)
            .clip(RectangleShape)
    ) {
        Row(
            Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EditAction(
                Modifier
                    .width(defaultActionSize),
                editAction
            )
            DeleteAction(
                Modifier
                    .width(defaultActionSize),
                deleteAction
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = -draggableState
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    draggableState,
                    Orientation.Horizontal,
                    reverseDirection = true,
                    enabled = dragEnabled),
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun resetAnchoredDraggableState(anchoredDraggableState: AnchoredDraggableState<DragAnchors>) {
    anchoredDraggableState.snapTo(DragAnchors.Start)
}
