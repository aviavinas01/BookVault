package com.example.bookvault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookvault.presentation.screens.AddBookScreen
import com.example.bookvault.presentation.screens.BookDetailScreen
import com.example.bookvault.presentation.screens.BrowseScreen
import com.example.bookvault.presentation.screens.HomeScreen
import com.example.bookvault.presentation.screens.SplashScreen
import com.example.bookvault.presentation.viewmodel.BookViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: BookViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onBrowseClick = {
                    navController.navigate(Screen.Browse.route)
                },
                onAddManuallyClick = {
                    navController.navigate(Screen.AddBook.route)
                },
                onBookClick = { bookId ->
                    viewModel.fetchBookById(bookId)
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                }
            )
        }

        composable(Screen.Browse.route) {
            BrowseScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBookClick = { bookId ->
                    viewModel.fetchBookById(bookId)
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.IntType }
            )
        ) {
            BookDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onDelete = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddBook.route) {
            AddBookScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}