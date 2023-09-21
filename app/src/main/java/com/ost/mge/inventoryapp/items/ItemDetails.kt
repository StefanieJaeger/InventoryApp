package com.ost.mge.inventoryapp.items

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ost.mge.inventoryapp.items.ui.theme.InventoryAppTheme

class ItemDetails : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val intent = (context as ItemDetails).intent
                    val itemName = intent.getStringExtra("itemName")
                    ShowForm(itemName ?: "-")
                }
            }
        }
    }
}

@Composable
fun ShowForm(itemName: String, modifier: Modifier = Modifier) {
    Text(text = "You can edit here $itemName")
}
