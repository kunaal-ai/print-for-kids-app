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
                onSubjectSelected = { subject ->
                    navController.navigate("operation/$grade/$subject")
                }
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
                    navController.navigate("preview/$grade/$subject/$operation")
                }
            )
        }
        composable(
            "preview/{grade}/{subject}/{operation}",
            arguments = listOf(
                navArgument("grade") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType },
                navArgument("operation") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            val subject = backStackEntry.arguments?.getString("subject") ?: "math"
            val operation = backStackEntry.arguments?.getString("operation") ?: "add"
            
            WorksheetPreviewScreen(grade, subject, operation)
        }
    }
}
