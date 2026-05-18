package com.example.bookvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.bookvault.presentation.navigation.NavGraph
import com.example.bookvault.presentation.ui.theme.BookVaultTheme
import com.example.bookvault.presentation.viewmodel.BookViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            BookVaultTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val viewModel: BookViewModel = koinViewModel()

                NavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}