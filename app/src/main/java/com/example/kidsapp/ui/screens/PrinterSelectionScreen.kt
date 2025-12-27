package com.example.kidsapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterSelectionScreen(
    currentPrinter: String?,
    onPrinterSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    var isScanning by remember { mutableStateOf(true) }
    
    // Mock printer list simulation
    val scannedPrinters = remember { mutableStateListOf<String>() }
    
    LaunchedEffect(Unit) {
        // Simulate scanning delay
        delay(800)
        scannedPrinters.add("HP DeskJet 3700 Series")
        delay(600)
        scannedPrinters.add("Canon PIXMA MG3620")
        delay(500)
        scannedPrinters.add("Brother HL-L2350DW")
        delay(1000)
        isScanning = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Printer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isScanning) {
                        IconButton(onClick = { 
                            isScanning = true 
                            scannedPrinters.clear()
                            // Restart scan simulation logic if needed or just simple toggle
                        }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Header / Status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isScanning) {
                        CircularProgressIndicator(
                            color = Color(0xFFFFC107),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Searching for printers...", color = Color.Gray)
                    } else {
                        Icon(
                            Icons.Default.Print, 
                            contentDescription = null, 
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "${scannedPrinters.size} printers found", 
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "AVAILABLE PRINTERS",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(scannedPrinters + "Save as PDF") { printer ->
                    PrinterItem(
                        name = printer,
                        isSelected = printer == currentPrinter,
                        onClick = { onPrinterSelected(printer) }
                    )
                }
            }
        }
    }
}

@Composable
fun PrinterItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFFFC107).copy(alpha = 0.2f) else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Print,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFFFFC107).copy(alpha = 1.0f).darken(0.4f) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color(0xFFFFC107).darken(0.4f))
            }
        }
    }
}

// Helper to darken color for contrast
fun Color.darken(factor: Float): Color {
    return Color(
        red = this.red * (1 - factor),
        green = this.green * (1 - factor),
        blue = this.blue * (1 - factor),
        alpha = this.alpha
    )
}
