package week11.st530550.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import week11.st530550.finalproject.ui.screens.BrowseScreen
import week11.st530550.finalproject.ui.screens.ForgotPasswordScreen
import week11.st530550.finalproject.ui.screens.LoginScreen
import week11.st530550.finalproject.ui.screens.MyPostsScreen
import week11.st530550.finalproject.ui.screens.PostLostItemScreen
import week11.st530550.finalproject.ui.screens.RegisterScreen
import week11.st530550.finalproject.viewmodel.SessionViewModel

@Composable
fun CampusLostFoundNavGraph(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = viewModel(),
) {
    val currentUser by sessionViewModel.currentUser.collectAsStateWithLifecycle()
    val startDestination = if (currentUser != null) Routes.BROWSE else Routes.LOGIN

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
        composable(Routes.BROWSE) {
            BrowseScreen(
                onPostLostItem = { navController.navigate(Routes.POST_LOST_ITEM) },
                onNavigateToMyPosts = { navController.navigate(Routes.MY_POSTS) },
                onSignedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.MY_POSTS) {
            MyPostsScreen(
                onNavigateToBrowse = { navController.popBackStack() },
                onEditItem = { itemId -> navController.navigate(Routes.editLostItem(itemId)) },
            )
        }
        composable(Routes.POST_LOST_ITEM) {
            PostLostItemScreen(
                editItemId = null,
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.EDIT_LOST_ITEM,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
        ) { backStackEntry ->
            PostLostItemScreen(
                editItemId = backStackEntry.arguments?.getString("itemId"),
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }
    }
}

/** Navigates to Browse and drops every auth screen from the back stack. */
private fun NavHostController.navigateClearingAuthStack() {
    navigate(Routes.BROWSE) {
        popUpTo(0) { inclusive = true }
    }
}
