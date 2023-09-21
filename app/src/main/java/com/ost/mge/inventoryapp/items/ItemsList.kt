package com.ost.mge.inventoryapp.items

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.DataSource
import com.ost.mge.inventoryapp.data.Item
import com.ost.mge.inventoryapp.items.ui.theme.InventoryAppTheme

class ItemsList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val intent = (context as ItemsList).intent
                    val categoryName = intent.getStringExtra("categoryName")
                    ShowList(items = DataSource().loadItemsForCategoryName(categoryName ?: "-"))
                }
            }
        }
    }
}

@Composable
fun ShowList(items: List<Item>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(items) { item ->
            ShowCard(item)
        }
    }
}

@Composable
fun ShowCard(item: Item, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card (modifier = Modifier.padding(8.dp)) {
        Button(onClick = {
            val intent = Intent(context, ItemDetails::class.java)
            intent.putExtra("itemName", item.name)
            context.startActivity(intent)
        }) {
            Column {
                Text(text=item.name, modifier = modifier)
            }
            Column {
                Text(text=item.description, modifier = modifier)
            }
        }
    }
}