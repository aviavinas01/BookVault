package com.example.bookvault.presentation.navigation

sealed class Screen(val route: String) {
    object Splash     : Screen("splash")
    object Home       : Screen("home")
    object Discover   : Screen("discover")
    object Profile    : Screen("profile")
    object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Int) = "book_detail/$bookId"
    }
    object AddBook    : Screen("add_book")
}