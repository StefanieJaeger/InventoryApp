package com.ost.mge.inventoryapp.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ost.mge.inventoryapp.State

@Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun CategoriesView(
    state: State,
    navController: NavHostController,
    createTestDate: () -> Unit
    ) {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when {
                    state.categories.isEmpty() -> {
                        Button(onClick = createTestDate) {
                            Text("DATA")
                        }
                    }

                    else -> {
                        LazyColumn {
                            items(state.categories) { category ->
                                Button(onClick = { navController.navigate("categories/$category/items") }) {
                                    Text(category)
                                }
                            }
                        }
                    }
                }
            }
        }
    }