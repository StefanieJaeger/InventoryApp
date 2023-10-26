package com.ost.mge.inventoryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditAction(modifier: Modifier, action: () -> Unit) {
    // todo: style with global theme
    Button(onClick = action,
        modifier = modifier
            .fillMaxHeight()
            .background(color = Color.Yellow)) {
        Icon(
            modifier = Modifier
            .padding(top = 10.dp, bottom = 4.dp)
            .padding(horizontal = 20.dp)
            .size(22.dp),
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit",
            tint = Color.White
        )
        Text(
            text = "Edit",
            color = Color.White,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun DeleteAction(modifier: Modifier, action: () -> Unit) {
    // todo: style with global theme
    // todo: button styles weird?
    Button(onClick = action,
        modifier = modifier
            .fillMaxHeight()
            .background(color = Color.Red)) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .padding(horizontal = 20.dp)
                    .size(22.dp),
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
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