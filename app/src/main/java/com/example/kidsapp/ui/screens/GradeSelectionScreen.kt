package com.example.kidsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kidsapp.ui.components.SelectionCard

// Data class for grades
data class GradeOption(val id: String, val label: String, val ages: String, val color: Color)

@Composable
fun GradeSelectionScreen(
    onGradeSelected: (String) -> Unit
) {
    val grades = listOf(
        GradeOption("pre", "Preschool", "Ages 3-4", Color(0xFF8D6E63)), // Brownish
        GradeOption("k", "Kindergarten", "Ages 5-6", Color(0xFFFBC02D)), // Yellowish
        GradeOption("1", "Grade 1", "Ages 6-7", Color(0xFF039BE5)), // Blue
        GradeOption("2", "Grade 2", "Ages 7-8", Color(0xFF00897B))  // Teal
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Step Indicators
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(24.dp, 8.dp).background(Color(0xFFFFC107), CircleShape))
                    Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                    Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Let's find the right fit!",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose a grade level to see personalized worksheets and activities.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(grades) { grade ->
                    SelectionCard(
                        label = grade.label,
                        subLabel = grade.ages,
                        color = grade.color,
                        onClick = { onGradeSelected(grade.id) }
                    )
                }
            }
        }
    }
}
