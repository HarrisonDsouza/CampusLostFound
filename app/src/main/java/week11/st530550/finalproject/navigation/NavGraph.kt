// Purpose: the app's Compose Navigation graph. Signed-in users land on Home, signed-out
// users land on Login; successful login/register clears the auth back-stack so Back
// doesn't return to the login screen once inside the app.
// Author: Harrison Dsouza
package week11.st530550.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import week11.st530550.finalproject.ui.screens.ForgotPasswordScreen
import week11.st530550.finalproject.ui.screens.HomeScreen
import week11.st530550.finalproject.ui.screens.LoginScreen
import week11.st530550.finalproject.ui.screens.RegisterScreen
import week11.st530550.finalproject.viewmodel.SessionViewModel

@Composable
fun CampusLostFoundNavGraph(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = viewModel(),
) {
    val currentUser by sessionViewModel.currentUser.collectAsStateWithLifecycle()
    val startDestination = if (currentUser != null) Routes.HOME else Routes.LOGIN

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigateClearingAuthStack() },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigateClearingAuthStack() },
                onNavigateToLogin = { navController.popBackStack() },
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onSignedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}

/** Navigates to Home and drops every auth screen from the back stack. */
private fun NavHostController.navigateClearingAuthStack() {
    navigate(Routes.HOME) {
        popUpTo(0) { inclusive = true }
    }
}
