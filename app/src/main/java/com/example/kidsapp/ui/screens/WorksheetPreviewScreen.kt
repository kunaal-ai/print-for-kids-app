package com.example.kidsapp.ui.screens

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kidsapp.utils.MathGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetPreviewScreen(
    grade: String,
    subject: String,
    operation: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val problems = remember(grade, operation) {
        MathGenerator.generateProblems(grade, operation)
    }

    // State for Print Settings
    var isColor by remember { androidx.compose.runtime.mutableStateOf(true) }
    var isLetterSize by remember { androidx.compose.runtime.mutableStateOf(true) } // True = Letter, False = A4
    var copies by remember { androidx.compose.runtime.mutableIntStateOf(1) } // Visual only, system handles copies

    // Generate HTML for the worksheet
    val htmlContent = remember(problems) {
        val sb = StringBuilder()
        sb.append("<html><head><style>")
        sb.append("body { font-family: sans-serif; padding: 20px; }")
        sb.append(".grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px; }")
        sb.append(".item { font-size: 24px; padding: 15px; border-bottom: 0px solid #ccc; }")
        sb.append("h1 { text-align: center; }")
        sb.append("</style></head><body>")
        sb.append("<h1>Math Worksheet ($grade)</h1>")
        sb.append("<div class='grid'>")
        problems.forEach { prob ->
            sb.append("<div class='item'>$prob</div>")
        }
        sb.append("</div>")
        sb.append("</body></html>")
        sb.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Print Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estimated cost",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Free",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { printWebView(context, htmlContent, isColor, isLetterSize) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107), // Yellow
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Filled.Print, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Print • 1 Page",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f), // Keep a paper-like aspect ratio
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewClient = WebViewClient()
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            setInitialScale(100)
                            loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings Section
            Text(
                text = "Print Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Printer Setting (Informational but Clickable)
            SettingRow(
                icon = Icons.Default.Print,
                title = "Printer",
                subtitle = "Select at final step",
                isReady = true,
                value = "Standard",
                onClick = { printWebView(context, htmlContent, isColor, isLetterSize) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Copies Setting (Visual Only - System handles this)
            SettingRow(
                icon = Icons.Default.FileCopy, 
                title = "Copies",
                valueComponent = {
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       modifier = Modifier.background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                   ) {
                       IconButton(onClick = { if (copies > 1) copies-- }) { 
                           Text("-", fontWeight = FontWeight.Bold) 
                       }
                       Text("$copies", fontWeight = FontWeight.Bold)
                       IconButton(
                           onClick = { copies++ },
                           colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFC107))
                       ) { 
                           Text("+", fontWeight = FontWeight.Bold, color = Color.Black) 
                       }
                   }
                }
            )
             Spacer(modifier = Modifier.height(12.dp))
             
             // Color Setting
            SettingRow(
                icon = Icons.Default.Palette,
                title = "Color",
                valueComponent = {
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       modifier = Modifier
                           .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                           .padding(4.dp)
                   ) {
                       Button(
                           onClick = { isColor = true }, 
                           colors = ButtonDefaults.buttonColors(
                               containerColor = if (isColor) Color.White else Color.Transparent, 
                               contentColor = if (isColor) Color.Black else Color.Gray
                           ),
                           shape = RoundedCornerShape(12.dp),
                           elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isColor) 2.dp else 0.dp),
                           contentPadding = PaddingValues(horizontal = 12.dp),
                           modifier = Modifier.height(32.dp)
                       ) { Text("Color", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                       
                       TextButton(
                           onClick = { isColor = false }, 
                           modifier = Modifier.height(32.dp),
                           colors = ButtonDefaults.textButtonColors(
                               contentColor = if (!isColor) Color.Black else Color.Gray
                           ),
                           // Hacky way to simulate selection style for the text button or just switch types? 
                           // For simplicity, let's just color the text correctly. 
                           // A better way is two Buttons with different states.
                       ) { Text("B&W", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                   }
                }
            )
             Spacer(modifier = Modifier.height(12.dp))

            // Paper Size Setting
             SettingRow(
                icon = Icons.Default.AspectRatio,
                title = "Paper Size",
                valueComponent = {
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       modifier = Modifier.background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                           .padding(horizontal = 12.dp, vertical = 6.dp)
                           .clickable { isLetterSize = !isLetterSize }
                   ) {
                       Text(
                           if (isLetterSize) "Letter (8.5x11)" else "ISO A4", 
                           fontSize = 14.sp, 
                           color = Color.Black, 
                           fontWeight = FontWeight.Bold
                       )
                       Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                   }
                }
            )

            Spacer(modifier = Modifier.height(100.dp)) // Space for scrolling under bottom bar
        }
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isReady: Boolean = false,
    value: String? = null,
    onClick: (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    valueComponent: (@Composable () -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFF8E1), CircleShape), // Light Yellow bg for icon
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isReady) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFF4CAF50), CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (value != null) {
                Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
            if (action != null) action()
            if (valueComponent != null) valueComponent()
        }
    }
}

fun printWebView(context: Context, htmlContent: String, isColor: Boolean, isLetterSize: Boolean) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
    val webView = WebView(context)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            val attributes = PrintAttributes.Builder()
                .setColorMode(if (isColor) PrintAttributes.COLOR_MODE_COLOR else PrintAttributes.COLOR_MODE_MONOCHROME)
                .setMediaSize(if (isLetterSize) PrintAttributes.MediaSize.NA_LETTER else PrintAttributes.MediaSize.ISO_A4)
                .build()

            printManager?.print(
                "Math_Worksheet",
                webView.createPrintDocumentAdapter("Worksheet"),
                attributes
            )
        }
    }
    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
}
