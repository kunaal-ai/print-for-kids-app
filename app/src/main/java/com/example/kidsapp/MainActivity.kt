package com.example.kidsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kidsapp.ui.screens.PersonalizationScreen
import com.example.kidsapp.ui.screens.GradeSelectionScreen
import com.example.kidsapp.ui.screens.OperationSelectionScreen
import com.example.kidsapp.ui.screens.SubjectSelectionScreen
import com.example.kidsapp.ui.screens.WorksheetPreviewScreen
import com.example.kidsapp.ui.theme.KidsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "grade") {
        composable("grade") {
            GradeSelectionScreen(
                onGradeSelected = { grade ->
                    navController.navigate("subject/$grade")
                }
            )
        }
        composable(
            "subject/{grade}",
            arguments = listOf(navArgument("grade") { type = NavType.StringType })
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            SubjectSelectionScreen(
                gradeId = grade,
                onSubjectSelected = { subject ->
                    if (grade == "pre") {
                        // Preschool skips operation selection, use subject as operation
                        navController.navigate("personalization/$grade/identification/$subject")
                    } else {
                        navController.navigate("operation/$grade/$subject")
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "operation/{grade}/{subject}",
            arguments = listOf(
                navArgument("grade") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            val subject = backStackEntry.arguments?.getString("subject") ?: "math"
            
            OperationSelectionScreen(
                onOperationSelected = { operation ->
                    navController.navigate("personalization/$grade/$subject/$operation")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "personalization/{grade}/{subject}/{operation}",
            arguments = listOf(
                navArgument("grade") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType },
                navArgument("operation") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            val subject = backStackEntry.arguments?.getString("subject") ?: "math"
            val operation = backStackEntry.arguments?.getString("operation") ?: "add"

            PersonalizationScreen(
                grade = grade,
                operation = operation,
                onPersonalizationCompleted = { difficulty, layoutType, coloringItem ->
                    navController.navigate("preview/$grade/$subject/$operation/$difficulty/$layoutType/$coloringItem")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "preview/{grade}/{subject}/{operation}/{difficulty}/{layoutType}/{coloringItem}",
            arguments = listOf(
                navArgument("grade") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType },
                navArgument("operation") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType },
                navArgument("layoutType") { type = NavType.StringType },
                navArgument("coloringItem") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            val subject = backStackEntry.arguments?.getString("subject") ?: "math"
            val operation = backStackEntry.arguments?.getString("operation") ?: "add"
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "1-10"
            val layoutType = backStackEntry.arguments?.getString("layoutType") ?: "worksheet_only"
            val coloringItem = backStackEntry.arguments?.getString("coloringItem") ?: "none"

            WorksheetPreviewScreen(
                grade = grade,
                subject = subject,
                operation = operation,
                difficulty = difficulty,
                layoutType = layoutType,
                coloringItem = coloringItem,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
