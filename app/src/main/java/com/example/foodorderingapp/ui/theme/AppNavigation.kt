package com.example.foodorderingapp.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodorderingapp.ui.auth.LoginScreen
import com.example.foodorderingapp.ui.menu.MenuScreen
import com.example.foodorderingapp.ui.order.OrderScreen

// Navigation routes constants
object NavigationRoutes {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val ORDER = "order"
}

@Composable
fun AppNavigation(
    contentPadding: PaddingValues,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Login Screen
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavigationRoutes.MENU) {
                        // Clear login screen from back stack to prevent going back
                        popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Menu Screen
        composable(NavigationRoutes.MENU) {
            MenuScreen(
                onOrderClick = {
                    navController.navigate(NavigationRoutes.ORDER)
                },
                onBackPressed = {
                    // Handle back press - you can exit app or navigate to login
                    // For now, we'll stay on menu screen or you can implement exit logic
                },
                onLogout = {
                    navController.navigate(NavigationRoutes.LOGIN) {
                        popUpTo(NavigationRoutes.MENU) { inclusive = true }
                    }
                }
            )
        }

        // Order Screen
        composable(NavigationRoutes.ORDER) {
            OrderScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onOrderPlaced = { orderItems, address, instructions, paymentMethod, total ->
                    // Navigate back to menu after order is placed
                    navController.navigate(NavigationRoutes.MENU) {
                        popUpTo(NavigationRoutes.ORDER) { inclusive = true }
                    }
                }
            )
        }
    }
}

// Alternative version with better separation of concerns
@Composable
fun AppNavigationWithViewModel(
    contentPadding: PaddingValues,
    navController: NavHostController = rememberNavController(),
    onExitApp: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Login Screen
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navigateToMenu(navController)
                }
            )
        }

        // Menu Screen
        composable(NavigationRoutes.MENU) {
            // Handle back press - exit app or show confirmation
            BackHandler {
                onExitApp()
            }

            MenuScreen(
                onOrderClick = {
                    navigateToOrder(navController)
                },
                onLogout = {
                    navigateToLogin(navController)
                }
            )
        }

        // Order Screen
        composable(NavigationRoutes.ORDER) {
            OrderScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onOrderPlaced = { orderItems, address, instructions, paymentMethod, total ->
                    navigateToMenu(navController)
                }
            )
        }
    }
}

// Navigation helper functions
private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(NavigationRoutes.LOGIN) {
        popUpTo(0) { inclusive = true }
    }
}

private fun navigateToMenu(navController: NavHostController) {
    navController.navigate(NavigationRoutes.MENU) {
        popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
    }
}

private fun navigateToOrder(navController: NavHostController) {
    navController.navigate(NavigationRoutes.ORDER)
}

// Additional screens you might want to add:
/*
composable(NavigationRoutes.PROFILE) {
    ProfileScreen(
        onBackPressed = { navController.popBackStack() },
        onLogout = { navigateToLogin(navController) }
    )
}

composable(NavigationRoutes.ORDER_HISTORY) {
    OrderHistoryScreen(
        onBackPressed = { navController.popBackStack() },
        onOrderClick = { orderId ->
            navController.navigate("${NavigationRoutes.ORDER_DETAILS}/$orderId")
        }
    )
}

composable("${NavigationRoutes.ORDER_DETAILS}/{orderId}") { backStackEntry ->
    val orderId = backStackEntry.arguments?.getString("orderId")
    OrderDetailsScreen(
        orderId = orderId,
        onBackPressed = { navController.popBackStack() }
    )
}
*/