package com.example.pokectcollection.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable function [SearchBar] that creates a search bar with a leading search icon.
 *
 * @param texto The placeholder text to display inside the search bar.
 * @param filterText The current text value of the search bar.
 * @param onFilterChange A callback function that gets called whenever the text in the search bar changes.
 */
@Composable
fun SearchBar(
    texto: String,
    filterText: String,
    onFilterChange: (String) -> Unit
) {
    OutlinedTextField(
        value = filterText,
        onValueChange = onFilterChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(texto) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        singleLine = true
    )
}
