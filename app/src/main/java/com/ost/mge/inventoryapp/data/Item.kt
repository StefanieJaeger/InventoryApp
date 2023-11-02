package com.ost.mge.inventoryapp.data

import android.net.Uri

// todo: We would like to store an image for an item as well
data class Item(val id: String, var name: String, var description: String, var imagePath: Uri)