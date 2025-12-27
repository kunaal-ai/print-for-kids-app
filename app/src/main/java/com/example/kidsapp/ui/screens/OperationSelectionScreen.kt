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
import com.example.kidsapp.ui.theme.darken

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

data class OperationOption(val id: String, val label: String, val icon: ImageVector?, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationSelectionScreen(
    onOperationSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val operations = listOf(
        OperationOption("add", "Addition", Icons.Rounded.Add, Color(0xFFFFCC80)),
        OperationOption("subtract", "Subtraction", Icons.Rounded.Remove, Color(0xFFEF9A9A)),
        OperationOption("multiply", "Multiplication", Icons.Rounded.Clear, Color(0xFFFFF59D)),
        OperationOption("divide", "Division", null, Color(0xFF90CAF9)) // Null icon means use symbol
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Math Operation",
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
                        Box(modifier = Modifier.size(24.dp, 8.dp).background(Color(0xFFFFC107), CircleShape))
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
                text = "Choose an Operation",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, lineHeight = 40.sp),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "What kind of math do you want to practice?",
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
                items(operations) { op ->
                    OperationCard(
                        operation = op,
                        onClick = { onOperationSelected(op.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun OperationCard(
    operation: OperationOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.9f)
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(operation.color.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (operation.icon != null) {
                    Icon(
                        imageVector = operation.icon,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = operation.color.copy(alpha = 1f).darken(0.4f)
                    )
                } else {
                    // Fallback for Division or others without standard icons
                    Text(
                        text = "÷",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = operation.color.copy(alpha = 1f).darken(0.4f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = operation.label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}


