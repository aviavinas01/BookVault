package com.example.bookvault.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        BottomNavItem(
            label = "Discover",
            route = Screen.Discover.route,
            icon = Icons.Outlined.Explore,
            selectedIcon = Icons.Filled.Explore
        ),
        BottomNavItem(
            label = "Shelf",
            route = Screen.Shelf.route,
            icon = Icons.Outlined.LibraryBooks,
            selectedIcon = Icons.Filled.LibraryBooks
        ),
        BottomNavItem(
            label = "Profile",
            route = Screen.Profile.route,
            icon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
            // bottomBar removed — pill is now a floating overlay below, so content
            // flows freely behind it and only the pill shape itself blocks the view.
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

        // Floating pill — overlays content, content scrolls freely behind it.
        BookVaultNavBar(
            items = navItems,
            currentRoute = currentDestination?.route,
            onItemSelected = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun BookVaultNavBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp)),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 0.dp,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BookVaultNavItem(
                        item = item,
                        selected = currentRoute == item.route,
                        onClick = { onItemSelected(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookVaultNavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
        else Color.Transparent,
        animationSpec = tween(220),
        label = "nav-bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
        animationSpec = tween(220),
        label = "nav-content"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = MaterialTheme.colorScheme.primary),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(tween(180)) + expandHorizontally(tween(220)),
            exit = fadeOut(tween(120)) + shrinkHorizontally(tween(180))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(7.dp))
                Text(
                    text = item.label,
                    color = contentColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
        }
    }
}