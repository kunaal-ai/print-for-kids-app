package com.example.kidsapp.ui.theme

import androidx.compose.ui.graphics.Color

// Helper to darken color for contrast
fun Color.darken(factor: Float): Color {
    return Color(
        red = this.red * (1 - factor),
        green = this.green * (1 - factor),
        blue = this.blue * (1 - factor),
        alpha = this.alpha
    )
}
