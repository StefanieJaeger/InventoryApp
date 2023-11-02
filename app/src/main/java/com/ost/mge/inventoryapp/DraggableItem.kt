package com.ost.mge.inventoryapp

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.ost.mge.inventoryapp.categories.DragAnchors
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableItem(
    onEdit: (() -> Unit)? = null,
    onDelete: () -> Unit,
    dragEnabled: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val totalButtonsWidth = if (onEdit != null) listItemHeight * 2 else listItemHeight
    val actionsSizePx = with(density) { totalButtonsWidth.toPx() }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.End at actionsSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { listItemHeight.toPx() } },
            animationSpec = tween(),
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val editAction = {
        coroutineScope.launch {
            resetAnchoredDraggableState(draggableState)
        }
        if(onEdit != null) {
            onEdit()
        }
    }

    val deleteAction = {
        coroutineScope.launch {
            resetAnchoredDraggableState(draggableState)
        }
        onDelete()
    }

    Box(
        modifier = Modifier
            .height(listItemHeight)
            .clip(RectangleShape)
    ) {
        Row(
            Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if(onEdit != null) {
                EditAction(
                    modifier = Modifier
                        .width(listItemHeight)
                        .height(listItemHeight),
                    action = editAction
                )
            }
            DeleteAction(
                modifier = Modifier
                    .width(listItemHeight)
                    .height(listItemHeight),
                action = deleteAction
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
                    enabled = dragEnabled
                ),
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun resetAnchoredDraggableState(anchoredDraggableState: AnchoredDraggableState<DragAnchors>) {
    anchoredDraggableState.snapTo(DragAnchors.Start)
}