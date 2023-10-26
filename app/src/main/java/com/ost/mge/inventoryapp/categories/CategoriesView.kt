package com.ost.mge.inventoryapp.categories

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.data.Category
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
    navController: NavHostController
    ) {
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    val filteredCategories = if(searchState.value.text.isEmpty()) categories else ArrayList();

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Categories") })
    })  { padding ->
        Column(Modifier.padding(padding)) {
            SearchView(searchState)
            LazyColumn {
                items(filteredCategories) { category ->
                    DraggableCategoryListItem(
                        category,
                        onItemClick = {navController.navigate("categories/${category.id}/items")}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableCategoryListItem(category: Category, onItemClick: (Category) -> Unit) {
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

    Row(Modifier.clickable(onClick = { onItemClick(category) })) {
        DraggableItem(
            draggableState,
            action = {
                Row(
                    Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    EditAction(
                        Modifier
                            .width(defaultActionSize)
                            .fillMaxHeight()
                    )
                    DeleteAction(
                        Modifier
                            .width(defaultActionSize)
                            .fillMaxHeight()
                    )
                }
            }
        ) {
            Box(Modifier
                // todo: use our themed color
                .background(Color.Green)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(category.name)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableItem(
    draggableState: AnchoredDraggableState<DragAnchors>,
    action: @Composable (BoxScope.() -> Unit)? = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(100.dp)
            .clip(RectangleShape)
    ) {
        action?.let {
            action()
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
                .anchoredDraggable(draggableState, Orientation.Horizontal, reverseDirection = true),
            content = content
        )
    }
}

@Composable
fun EditAction(modifier: Modifier) {
    Box(
        modifier = modifier.background(color = Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = Color.White
            )

            Text(
                text = "Edit",
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun DeleteAction(modifier: Modifier) {
    Box(
        modifier = modifier.background(color = Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Color.White
            )

            Text(
                text = "Delete",
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun SearchView(searchState: MutableState<TextFieldValue>) {
    TextField(
        value = searchState.value,
        onValueChange = {value -> searchState.value = value },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RectangleShape,
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(22.dp)
            )
        },
        trailingIcon = {
            if (searchState.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        searchState.value =
                            TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
//        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color.White,
//            cursorColor = Color.White,
//            leadingIconColor = Color.White,
//            trailingIconColor = Color.White,
//            backgroundColor = colorResource(id = R.color.colorPrimary),
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            disabledIndicatorColor = Color.Transparent
//        )
    )
}