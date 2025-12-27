package com.example.kidsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

data class SubjectOption(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val isEnabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectSelectionScreen(
    gradeId: String,
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    // Map gradeId to display text (e.g., "1" -> "1st Grade")
    val gradeDisplay = when (gradeId) {
        "pre" -> "Preschool"
        "k" -> "Kindergarten"
        "1" -> "1st Grade"
        "2" -> "2nd Grade"
        else -> "Grade $gradeId"
    }

    val subjects = listOf(
        SubjectOption("math", "Math", Icons.Default.Calculate, Color(0xFFFFCC80), isEnabled = true), // Orange-ish
        SubjectOption("english", "Reading", Icons.Default.MenuBook, Color(0xFF90CAF9), isEnabled = false), // Blue
        SubjectOption("science", "Science", Icons.Default.Science, Color(0xFFA5D6A7), isEnabled = false), // Green
        SubjectOption("art", "Art", Icons.Default.Brush, Color(0xFFE1BEE7), isEnabled = false), // Purple
        SubjectOption("writing", "Writing", Icons.Default.Edit, Color(0xFFEF9A9A), isEnabled = false), // Red
        SubjectOption("social", "Social Studies", Icons.Default.Public, Color(0xFF80DEEA), isEnabled = false) // Cyan
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pick a Subject",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                   // Step dots could go here, mirroring the previous screen style
                   Row(
                       modifier = Modifier.padding(end = 16.dp),
                       horizontalArrangement = Arrangement.spacedBy(8.dp)
                   ) {
                       Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                       Box(modifier = Modifier.size(24.dp, 8.dp).background(Color(0xFFFFC107), CircleShape))
                       Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                   }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What are we learning today?",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, lineHeight = 40.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Showing materials for ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = gradeDisplay,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 100.dp), // Space for FAB
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(subjects) { subject ->
                    SubjectCard(
                        subject = subject,
                        isSelected = false,
                        onClick = { 
                            if (subject.isEnabled) {
                                onSubjectSelected(subject.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: SubjectOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFFFFC107) else Color.Transparent
    val borderWidth = if (isSelected) 3.dp else 0.dp
    val alpha = if (subject.isEnabled) 1f else 0.5f

    Card(
        modifier = Modifier
            .aspectRatio(0.9f) // Almost square
            .clickable(enabled = subject.isEnabled) { onClick() },
        shape = RoundedCornerShape(32.dp), // Very round corners
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = alpha)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (subject.isEnabled) 4.dp else 0.dp),
        border = androidx.compose.foundation.BorderStroke(borderWidth, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(subject.color.copy(alpha = 0.3f * alpha), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = subject.icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = subject.color.copy(alpha = 1f * alpha).compositeOver(Color.Gray)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = subject.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = alpha)
            )
            
            if (!subject.isEnabled) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Extension to darken a color slightly for the icon
fun Color.compositeOver(background: Color): Color {
    val alpha = this.alpha
    val r = (this.red * alpha) + (background.red * (1 - alpha))
    val g = (this.green * alpha) + (background.green * (1 - alpha))
    val b = (this.blue * alpha) + (background.blue * (1 - alpha))
    return Color(r, g, b, 1f)
}
