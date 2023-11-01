package com.ost.mge.inventoryapp.items

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ost.mge.inventoryapp.BuildConfig
import com.ost.mge.inventoryapp.data.Item
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ItemView(
    navController: NavController,
    item: Item,
    onUpdateItem: (item: Item) -> Unit,
) {
    var itemNameField by remember { mutableStateOf(item.name) }
    var itemDescriptionField by remember { mutableStateOf(item.description) }
    var imageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) } // TODO: add imageuri to item

    val context = LocalContext.current
    val newFile = context.createImageFile()
    val contentUri: Uri = getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", newFile
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            imageUri = contentUri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(contentUri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = item.name) },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp)
                    {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        }
    )
    { innerpadding ->
        Column {
            TextField(
                value = itemNameField,
                onValueChange = {
                    itemNameField = it
                    item.name = itemNameField
                    onUpdateItem(item)
                },
                label = { Text(text = "Name") },
                placeholder = { Text(text = "Add a name") },
                modifier = Modifier.padding(innerpadding),
            )
            TextField(
                value = itemDescriptionField,
                onValueChange = {
                    itemDescriptionField = it
                    item.description = itemDescriptionField
                    onUpdateItem(item)
                },
                label = { Text(text = "Description") },
                placeholder = { Text(text = "Add a description") },
                modifier = Modifier.padding(innerpadding)
            )
            Button(
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(contentUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                content = { Text(text = "Add an image") },
            )

            val uriNotEmpty =
                "Is new Image Uri present? " + (imageUri.path?.isNotEmpty() == true).toString()

            Text(text = uriNotEmpty)

            if (imageUri.path?.isNotEmpty() == true) {
                Image(
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                    painter = rememberImagePainter(imageUri),
                    contentDescription = null
                )
            }
        }
    }
}

// needed, otherwise not working with only the two below before contentUri
//val imagePath: File = File(context.externalCacheDir, "my_images")
//val newFile = File(imagePath, "default_image.jpg")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}