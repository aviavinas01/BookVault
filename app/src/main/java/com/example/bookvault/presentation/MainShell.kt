package com.example.bookvault.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookvault.presentation.navigation.Screen
import com.example.bookvault.presentation.screens.BrowseScreen
import com.example.bookvault.presentation.screens.HomeScreen
import com.example.bookvault.presentation.screens.ProfileScreen
import com.example.bookvault.presentation.screens.ShelfScreen
import com.example.bookvault.presentation.viewmodel.BookViewModel

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector = icon
)

@Composable
fun MainShell(
    navController: NavHostController,
    viewModel: BookViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBookClick: (Int) -> Unit,
    onAddManuallyClick: () -> Unit
) {
    val navItems = listOf(
        BottomNavItem(
            label = "Home",
            route = Screen.Home.route,
            icon = Icons.Rounded.Home
        ),
        BottomNavItem(
            label = "Discover",
            route = Screen.Discover.route,
            icon = Icons.Rounded.AutoStories
        ),
        BottomNavItem(
            label = "Shelf",
            route = Screen.Shelf.route,
            icon = Icons.Rounded.MenuBook
        ),
        BottomNavItem(
            label = "Profile",
            route = Screen.Profile.route,
            icon = Icons.Rounded.Person
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets(0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                ) {
                    navItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold
                                    else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Swap content based on selected tab
            when (currentDestination?.route) {
                Screen.Discover.route -> {
                    BrowseScreen(
                        viewModel = viewModel,
                        onBack = {},
                        onBookClick = onBookClick
                    )
                }
                Screen.Shelf.route -> {
                    ShelfScreen(
                        viewModel = viewModel,
                        onBookClick = onBookClick
                    )
                }
                Screen.Profile.route -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        onBookClick = onBookClick
                    )
                }
                else -> {
                    HomeScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = onThemeToggle,
                        onBrowseClick = {
                            navController.navigate(Screen.Discover.route) {
                                launchSingleTop = true
                            }
                        },
                        onAddManuallyClick = onAddManuallyClick,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}