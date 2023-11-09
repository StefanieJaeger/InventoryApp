package com.ost.mge.inventoryapp.data

import android.net.Uri
import com.ost.mge.inventoryapp.serializer.UriAsStringSerializer
import kotlinx.serialization.Serializable

// todo: We would like to store an image for an item as well
@Serializable
data class Item(
    val id: Int,
    var name: String,
    var description: String,
    @Serializable(with = UriAsStringSerializer::class)
    var imagePath: Uri,
    var categoryId: Int
)