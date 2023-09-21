package com.ost.mge.inventoryapp.categories

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
import androidx.compose.ui.unit.dp
import com.ost.mge.inventoryapp.data.Category
import com.ost.mge.inventoryapp.data.DataSource
import com.ost.mge.inventoryapp.items.ItemsList
import com.ost.mge.inventoryapp.ui.theme.InventoryAppTheme

class CategoriesList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   ShowList(categories = DataSource().loadCategories())
                }
            }
        }
    }
}

@Composable
fun ShowList(categories: List<Category>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(categories) { category ->
            ShowCard(category)
        }
    }
}

@Composable
fun ShowCard(category: Category, modifier: Modifier = Modifier) {
    val context = LocalContext.current;

    Card (modifier = Modifier.padding(8.dp)) {
        Column {
            Text(text=category.name, modifier = modifier)
        }
        Column {
            Button(onClick = {
                val intent = Intent(context, ItemsList::class.java)
                intent.putExtra("categoryName", category.name)
                context.startActivity(intent)
            }){
                Text(text=">")
            }
        }
    }
}