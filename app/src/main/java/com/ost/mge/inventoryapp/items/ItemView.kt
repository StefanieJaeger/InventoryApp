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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
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
    onUpdateItem: (item: Item) -> Unit
) {
    var itemNameField by remember { mutableStateOf(item.name) }
    var itemDescriptionField by remember { mutableStateOf(item.description) }
    var imageUri by remember { mutableStateOf(item.imagePath) }
    var imageButtonText =
        if (imageUri.path?.isNotEmpty() == true) "Change image" else "Add an image"

    val context = LocalContext.current
    val newFile = context.createImageFile()
    val contentUri: Uri = getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", newFile
    )

    val deleteItemImageUri = {
        imageUri = "".toUri()
        item.imagePath = imageUri
        onUpdateItem(item)
    }

    val updateItemImageUri = {
        item.imagePath = imageUri
        onUpdateItem(item)
    }

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
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(contentUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                icon = { Icon(Icons.Filled.AddCircle, "Extended floating action button.") },
                text = { Text(text = imageButtonText) },
                //contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                //content = { Text(text = "Add an image") },
            )
        }
    )
    { innerpadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = itemNameField,
                onValueChange = {
                    itemNameField = it
                    item.name = itemNameField
                    onUpdateItem(item)
                },
                label = { Text(text = "Name") },
                placeholder = { Text(text = "Add a name") },
                modifier = Modifier
                    .padding(innerpadding)
                    .fillMaxWidth()
            )
            TextField(
                value = itemDescriptionField,
                onValueChange = {
                    itemDescriptionField = it
                    item.description = itemDescriptionField
                    onUpdateItem(item)
                },
                minLines = 5,
                maxLines = 5,
                label = { Text(text = "Description") },
                placeholder = { Text(text = "Add a description") },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
            )

            if (imageUri.path?.isNotEmpty() == true) {
                updateItemImageUri()
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(
                        onClick = { deleteItemImageUri() },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        content = { Text(text = "delete image") },
                        modifier= Modifier.align(Alignment.CenterHorizontally)
                    )
                    Image(
                        modifier = Modifier
                            .padding(16.dp, 8.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        painter = rememberImagePainter(imageUri),
                        contentDescription = null
                    )
                }
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