package com.example.bookvault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookvault.presentation.MainShell
import com.example.bookvault.presentation.screens.AddBookScreen
import com.example.bookvault.presentation.screens.BookDetailScreen
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

        // All bottom nav tabs share MainShell
        // MainShell internally switches content based on current route
        // BookDetail and AddBook push on top of shell without bottom nav
        listOf(
            Screen.Home.route,
            Screen.Discover.route,
            Screen.Shelf.route,
            Screen.Profile.route
        ).forEach { route ->
            composable(route) {
                MainShell(
                    navController = navController,
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    onBookClick = { bookId ->
                        viewModel.fetchBookById(bookId)
                        navController.navigate(Screen.BookDetail.createRoute(bookId))
                    },
                    onAddManuallyClick = {
                        navController.navigate(Screen.AddBook.route)
                    }
                )
            }
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.IntType }
            )
        ) {
            BookDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
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