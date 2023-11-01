package com.ost.mge.inventoryapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EditAction(modifier: Modifier, action: () -> Unit) {
    BaseAction(modifier, action, MaterialTheme.colorScheme.tertiary, Icons.Default.Edit, "Edit")
}

@Composable
fun DeleteAction(modifier: Modifier, action: () -> Unit) {
    BaseAction(modifier, action, MaterialTheme.colorScheme.error, Icons.Default.Delete, "Delete")

}

@Composable
private fun BaseAction(
    modifier: Modifier,
    action: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    text: String
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(onClick = action),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(spaceOneAndQuarter, spaceHalf, spaceOneAndQuarter, spaceQuarter)
                    .size(spaceOneAndQuarter),
                imageVector = icon,
                contentDescription = text,
                tint = Color.White
            )
            Text(
                text = text,
                color = Color.White,
            )
        }
    }
}