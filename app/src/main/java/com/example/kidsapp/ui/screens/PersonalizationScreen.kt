package com.example.kidsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Description
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kidsapp.ui.theme.darken

data class DifficultyOption(val id: String, val label: String, val color: Color)
data class LayoutOption(val id: String, val label: String, val description: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PersonalizationScreen(
    grade: String,
    operation: String,
    onPersonalizationCompleted: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    val difficulties: List<DifficultyOption> = remember(grade, operation) {
        getDifficultiesForGrade(grade, operation)
    }
    
    var selectedDifficulty by remember(difficulties) { 
        mutableStateOf(difficulties.firstOrNull()?.id ?: "1-10") 
    }
    var selectedLayout by remember { mutableStateOf("worksheet_only") }
    var selectedColoringItem by remember { mutableStateOf("Panda 🐼") }

    val coloringCategories = listOf(
        "Animals" to listOf("Panda 🐼", "Kangaroo 🦘", "Dog 🐶", "Cat 🐱", "Bunny 🐰"),
        "Delicious" to listOf("Ice-cream 🍦", "Candy 🍭"),
        "Nature" to listOf("Tree 🌳", "Leaf 🌿", "Cloud ☁️"),
        "Healthy" to listOf("Broccoli 🥦")
    )

    val layouts: List<LayoutOption> = listOf(
        LayoutOption("worksheet_only", "Worksheet Only", "Full page of math problems", Icons.Default.Description),
        LayoutOption("score_box", "Score Rating Box", "Adds a score section at the bottom", Icons.Default.Star),
        LayoutOption("drawing_area", "Drawing Area", "Adds a fun drawing zone for kids", Icons.Default.Brush)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Personalization",
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
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                        Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                        Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
                        Box(modifier = Modifier.size(24.dp, 8.dp).background(Color(0xFFFFC107), CircleShape))
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { 
                        onPersonalizationCompleted(
                            selectedDifficulty, 
                            selectedLayout,
                            if (selectedLayout == "drawing_area") selectedColoringItem else "none"
                        ) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text(
                        "Generate Worksheet",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            val showDifficulty = !(grade == "pre" && operation != "numbers")
            
            if (showDifficulty) {
                Text(
                    text = "Difficulty Level",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    difficulties.forEach { difficulty ->
                        FilterChip(
                            selected = selectedDifficulty == difficulty.id,
                            onClick = { selectedDifficulty = difficulty.id },
                            label = { Text(difficulty.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = difficulty.color.copy(alpha = 0.4f),
                                selectedLabelColor = Color.Black,
                                containerColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = Color.LightGray,
                                selectedBorderColor = difficulty.color.darken(0.3f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // If difficulty is hidden, show a nice message
                Text(
                    text = "Activity: ${operation.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No difficulty selection needed for this activity. It will generate a full set for practice!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            Text(
                text = "Worksheet Layout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            layouts.forEach { layout ->
                SelectableCard(
                    label = layout.label,
                    subtitle = layout.description,
                    isSelected = selectedLayout == layout.id,
                    onClick = { selectedLayout = layout.id },
                    color = Color(0xFFE1F5FE)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            if (selectedLayout == "drawing_area") {
                Spacer(modifier = Modifier.height(32.dp))
                Surface(
                    color = Color(0xFFFFFDE7),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Coloring Item",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Choose something fun for drawing time!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        coloringCategories.forEach { (category, items) ->
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items.forEach { item ->
                                    FilterChip(
                                        selected = selectedColoringItem == item,
                                        onClick = { selectedColoringItem = item },
                                        label = { Text(item) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFFC107).copy(alpha = 0.4f),
                                            selectedLabelColor = Color.Black,
                                            containerColor = Color.White
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Composable
fun SelectableCard(
    label: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.5f) else Color.White
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color.darken(0.2f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = color.darken(0.3f))
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

fun getDifficultiesForGrade(grade: String, operation: String): List<DifficultyOption> {
    if (grade == "pre") {
        return when (operation) {
            "alphabets" -> listOf(
                DifficultyOption("uppercase", "Uppercase (A-Z)", Color(0xFFA5D6A7)),
                DifficultyOption("lowercase", "Lowercase (a-z)", Color(0xFF90CAF9)),
                DifficultyOption("both", "Both Mixed", Color(0xFFFFCC80))
            )
            "shapes" -> listOf(
                DifficultyOption("basic_shapes", "Basic Shapes", Color(0xFFA5D6A7))
            )
            "colors" -> listOf(
                DifficultyOption("basic_colors", "Basic Colors", Color(0xFFA5D6A7))
            )
            else -> listOf( // Default to numbers
                DifficultyOption("1-5", "Level 1: 1-5", Color(0xFFA5D6A7)),
                DifficultyOption("5-10", "Level 2: 5-10", Color(0xFF90CAF9)),
                DifficultyOption("1-10", "Level 3: 1-10", Color(0xFFFFCC80)),
                DifficultyOption("1-20", "Level 4: 1-20", Color(0xFFEF9A9A))
            )
        }
    }
    
    return when (grade) {
        "k" -> listOf(
            DifficultyOption("1-10", "Level 1: 1-10", Color(0xFFA5D6A7)),
            DifficultyOption("1-20", "Level 2: 1-20", Color(0xFFFFF59D))
        )
        "1" -> listOf(
            DifficultyOption("1-20", "Level 1: 1-20", Color(0xFFA5D6A7)),
            DifficultyOption("1-50", "Level 2: 1-50", Color(0xFFFFF59D)),
            DifficultyOption("1-100", "Level 3: 1-100", Color(0xFFFFCC80))
        )
        "2" -> listOf(
            DifficultyOption("1-100", "Level 1: 1-100", Color(0xFFA5D6A7)),
            DifficultyOption("1-200", "Level 2: 1-200", Color(0xFFFFF59D)),
            DifficultyOption("1-500", "Level 3: 1-500", Color(0xFFFFCC80))
        )
        else -> listOf(
            DifficultyOption("1-10", "Level 1: 1-10", Color(0xFFA5D6A7)),
            DifficultyOption("1-20", "Level 2: 1-20", Color(0xFFFFF59D))
        )
    }
}
