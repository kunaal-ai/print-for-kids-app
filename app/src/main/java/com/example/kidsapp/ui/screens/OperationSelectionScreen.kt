package com.example.kidsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kidsapp.ui.components.SelectionCard

data class OperationOption(val id: String, val label: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationSelectionScreen(
    onOperationSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val operations = listOf(
        OperationOption("add", "➕", Color(0xFFA5D6A7)), // Green
        OperationOption("subtract", "➖", Color(0xFFEF9A9A)), // Red
        OperationOption("multiply", "✖️", Color(0xFFFFF59D)), // Yellow
        OperationOption("divide", "➗", Color(0xFF90CAF9))  // Blue
    )

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Math Operation") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(operations) { op ->
                    SelectionCard(
                        label = op.label,
                        color = op.color,
                        onClick = { onOperationSelected(op.id) }
                    )
                }
            }
        }
    }
}
