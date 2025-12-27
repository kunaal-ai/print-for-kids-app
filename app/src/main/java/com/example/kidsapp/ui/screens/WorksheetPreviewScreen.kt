package com.example.kidsapp.ui.screens

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kidsapp.utils.MathGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetPreviewScreen(
    grade: String,
    subject: String,
    operation: String,
    difficulty: String? = null,
    layoutType: String = "worksheet_only",
    coloringItem: String = "none",
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isIdentification = grade == "pre" || operation == "identification" || operation == "numbers" || operation == "alphabets" || operation == "shapes" || operation == "colors"
    
    val problemsCount = when {
        isIdentification && layoutType == "worksheet_only" -> 18
        isIdentification -> 12
        layoutType == "worksheet_only" -> 18
        else -> 12
    }

    val problems = remember(grade, operation, difficulty, layoutType) {
        MathGenerator.generateProblems(grade, operation, difficulty, count = problemsCount)
    }

    // Generate HTML for the worksheet
    val htmlContent = remember(problems, grade, operation, difficulty, layoutType, coloringItem) {
        val sb = StringBuilder()
        sb.append("<html><head><style>")
        
        // Base variables based on layout
        val idType = if (grade == "pre") operation else "none"
        val idFontSize = when (idType) {
            "numbers", "alphabets" -> "60px"
            "shapes", "colors" -> "35px"
            else -> "60px"
        }

        val gridGap = if (isIdentification) "20px" else if (layoutType == "worksheet_only") "40px" else "20px"
        val itemPadding = when {
            isIdentification && layoutType != "worksheet_only" -> "15px 10px"
            isIdentification -> "25px 10px"
            layoutType == "worksheet_only" -> "35px 20px"
            else -> "20px 10px"
        }
        val itemFontSize = if (isIdentification) idFontSize else if (layoutType == "worksheet_only") "38px" else "32px"
        val gridCols = "1fr 1fr 1fr"
        
        sb.append("""
            * {
                box-sizing: border-box;
            }
            @page {
                size: portrait;
                margin: 0;
            }
            body { 
                font-family: 'Comic Sans MS', 'Chalkboard SE', 'Marker Felt', sans-serif; 
                padding: 40px; 
                margin: 0;
                color: #333;
                background-color: #fff;
                display: flex;
                flex-direction: column;
                height: 100vh;
                max-height: 100vh;
                overflow: hidden;
                width: 100vw;
                border: 10px solid #FFC107;
                border-radius: 0;
                -webkit-print-color-adjust: exact;
            }
            .header {
                display: flex;
                justify-content: space-between;
                align-items: baseline;
                border-bottom: 3px dashed #FFC107;
                padding-bottom: 10px;
                margin-bottom: 15px;
            }
            .name-field {
                font-size: 18px;
                font-weight: bold;
                color: #555;
            }
            .logo {
                font-size: 14px;
                font-weight: 900;
                color: #FFC107;
                letter-spacing: 0.5px;
                text-transform: uppercase;
            }
            .info {
                text-align: center;
                margin-bottom: 15px;
                color: #777;
                font-size: 14px;
            }
            .grid { 
                display: grid; 
                grid-template-columns: $gridCols; 
                gap: $gridGap; 
                flex-grow: 1;
            }
            .item { 
                font-size: $itemFontSize; 
                padding: $itemPadding; 
                border: 2px solid #EEE;
                border-radius: 12px;
                text-align: center;
                background-color: #FAFAFA;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
            }
            .footer {
                margin-top: auto;
                text-align: center;
            }
            .extra-area {
                margin-top: 15px;
                border: 3px dashed #E0E0E0;
                border-radius: 20px;
                padding: 15px;
                background-color: #FFFDE7;
            }
            .score-title {
                font-size: 20px;
                font-weight: bold;
                color: #FF5722;
                margin-bottom: 10px;
            }
            .stars {
                font-size: 30px;
                color: #FFC107;
                letter-spacing: 10px;
            }
            .coloring-item {
                font-size: 120px;
                text-align: center;
                display: block;
                margin: 0 auto;
            }
            .coloring-label {
                font-size: 24px;
                color: #444;
                text-align: center;
                font-weight: bold;
            }
        """.trimIndent())
        sb.append("</style></head><body>")
        
        // Header
        sb.append("<div class='header'>")
        sb.append("<div class='name-field'>Name: __________________________</div>")
        sb.append("<div class='logo'>Print for kids</div>")
        sb.append("</div>")

        // Info
        sb.append("<div class='info'>")
        val operationLabel = operation.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        sb.append("$operationLabel Identification | Level: $difficulty | Grade: Preschool")
        sb.append("</div>")

        // Content
        sb.append("<div class='grid'>")
        problems.forEach { prob ->
            sb.append("<div class='item'>$prob</div>")
        }
        sb.append("</div>")
        
        // Conditional Layout Elements
        when (layoutType) {
            "score_box" -> {
                sb.append("<div class='extra-area'>")
                sb.append("<div class='score-title'>Teacher's / Parent's Corner</div>")
                sb.append("<div style='display:flex; justify-content:space-between; align-items:center;'>")
                sb.append("<div style='font-size:24px;'>Score: ________ / ${problems.size}</div>")
                sb.append("<div class='stars'>☆☆☆☆☆</div>")
                sb.append("</div>")
                sb.append("</div>")
            }
            "drawing_area" -> {
                sb.append("<div class='extra-area' style='height: auto; min-height: 220px;'>")
                sb.append("<div class='score-title'>Fun Drawing & Coloring Zone!</div>")
                if (coloringItem != "none") {
                    val emoji = coloringItem.split(" ").lastOrNull() ?: "🎨"
                    val name = coloringItem.split(" ").firstOrNull() ?: "this"
                    sb.append("<div class='coloring-item'>$emoji</div>")
                    sb.append("<div class='coloring-label'>Color the $name!</div>")
                } else {
                    sb.append("<div style='color:#AAA; text-align:center; padding-top:40px;'>Draw something awesome here! 🎨</div>")
                }
                sb.append("</div>")
            }
        }
        
        // Footer (Minimized)
        sb.append("<div class='footer'>")
        sb.append("<div style='color:#CCC; font-size:10px;'>Generated by Print for kids</div>")
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
                Button(
                    onClick = { printWebView(context, htmlContent) },
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
                        text = "Print",
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
            Spacer(modifier = Modifier.height(100.dp)) // Space for scrolling under bottom bar
        }
    }
}

fun printWebView(context: Context, htmlContent: String) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
    val webView = WebView(context)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            // Use default attributes, user can change in system dialog
            val attributes = PrintAttributes.Builder().build()

            printManager?.print(
                "Math_Worksheet",
                webView.createPrintDocumentAdapter("Worksheet"),
                attributes
            )
        }
    }
    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
}
