package com.example.bookvault.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookvault.presentation.screens.AddBookScreen
import com.example.bookvault.presentation.screens.BookDetailScreen
import com.example.bookvault.presentation.screens.BookListScreen
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
                onNavigateToList = {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BookList.route) {
            BookListScreen(
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onBookClick = { bookId ->
                    viewModel.fetchBookById(bookId)
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onAddClick = {
                    navController.navigate(Screen.AddBook.route)
                }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStack ->
            val bookId = backStack.arguments?.getInt("bookId") ?: return@composable
            BookDetailScreen(
                bookId = bookId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onDelete = {
                    viewModel.deleteBook(bookId)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AddBook.route) {
            AddBookScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}