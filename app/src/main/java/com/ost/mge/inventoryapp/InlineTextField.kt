package com.ost.mge.inventoryapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun InlineTextField(originalValue: String, onDone: (String) -> Unit) {
    val textFieldState = remember { mutableStateOf(TextFieldValue(originalValue)) }
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone(textFieldState.value.text)
            }
        ),
        singleLine = true,
    )
}