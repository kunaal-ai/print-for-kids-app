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
import com.example.kidsapp.ui.screens.PrinterSelectionScreen
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
                    navController.navigate("operation/$grade/$subject")
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
                    navController.navigate("preview/$grade/$subject/$operation")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "preview/{grade}/{subject}/{operation}?printer={printer}",
            arguments = listOf(
                navArgument("grade") { type = NavType.StringType },
                navArgument("subject") { type = NavType.StringType },
                navArgument("operation") { type = NavType.StringType },
                navArgument("printer") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val grade = backStackEntry.arguments?.getString("grade") ?: "k"
            val subject = backStackEntry.arguments?.getString("subject") ?: "math"
            val operation = backStackEntry.arguments?.getString("operation") ?: "add"
            // Get any returned printer name from savedStateHandle if navigating back
            val savedStateHandle = backStackEntry.savedStateHandle
            val selectedPrinter = savedStateHandle.get<String>("selected_printer") 
                ?: backStackEntry.arguments?.getString("printer")

            WorksheetPreviewScreen(
                grade = grade,
                subject = subject,
                operation = operation,
                selectedPrinterName = selectedPrinter,
                onChangePrinter = { current ->
                     navController.navigate("printer_selection?current=$current")
                },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            "printer_selection?current={current}",
            arguments = listOf(navArgument("current") { nullable = true })
        ) { backStackEntry ->
             val current = backStackEntry.arguments?.getString("current")
             PrinterSelectionScreen(
                 currentPrinter = current,
                 onPrinterSelected = { printer ->
                     // Pass result back
                     navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_printer", printer)
                     navController.popBackStack()
                 },
                 onBack = { navController.popBackStack() }
             )
        }
    }
}
