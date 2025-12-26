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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kidsapp.ui.components.SelectionCard

// Data class for grades
data class GradeOption(val id: String, val label: String, val color: Color)

@Composable
fun GradeSelectionScreen(
    onGradeSelected: (String) -> Unit
) {
    val grades = listOf(
        GradeOption("pre", "Pre", Color(0xFFFFCC80)),
        GradeOption("k", "K", Color(0xFFA5D6A7)),
        GradeOption("1", "1", Color(0xFF90CAF9)),
        GradeOption("2", "2", Color(0xFFCE93D8))
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Grade",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(grades) { grade ->
                    SelectionCard(
                        label = grade.label,
                        color = grade.color,
                        onClick = { onGradeSelected(grade.id) }
                    )
                }
            }
        }
    }
}
