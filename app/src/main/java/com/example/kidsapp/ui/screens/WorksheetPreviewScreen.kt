package com.example.kidsapp.ui.screens

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kidsapp.utils.MathGenerator

@Composable
fun WorksheetPreviewScreen(
    grade: String,
    subject: String,
    operation: String
) {
    val context = LocalContext.current
    val problems = remember(grade, operation) {
        MathGenerator.generateProblems(grade, operation)
    }

    // Generate HTML for the worksheet (easiest way to print nicely)
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { printWebView(context, htmlContent) }
            ) {
                Icon(Icons.Filled.Print, contentDescription = "Print")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewClient = WebViewClient()
                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    }
                }
            )
        }
    }
}

fun printWebView(context: Context, htmlContent: String) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
    val webView = WebView(context)
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            // Once loaded, print
            printManager?.print(
                "Math_Worksheet",
                webView.createPrintDocumentAdapter("Worksheet"),
                PrintAttributes.Builder().build()
            )
        }
    }
    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
}
