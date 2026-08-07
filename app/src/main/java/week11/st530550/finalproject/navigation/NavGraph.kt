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
import week11.st530550.finalproject.ui.screens.MatchReviewScreen
import week11.st530550.finalproject.ui.screens.MyPostsScreen
import week11.st530550.finalproject.ui.screens.PostItemScreen
import week11.st530550.finalproject.ui.screens.ProfileScreen
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
                onPostItem = { kind -> navController.navigate(Routes.postItem(kind)) },
                onNavigateToMyPosts = { navController.navigate(Routes.MY_POSTS) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onViewMatch = { lostItemId, foundItemId ->
                    navController.navigate(Routes.matchReview(lostItemId, foundItemId))
                },
            )
        }
        composable(Routes.MY_POSTS) {
            MyPostsScreen(
                onNavigateToBrowse = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onEditItem = { itemId -> navController.navigate(Routes.editItem(itemId)) },
                onViewMatch = { lostItemId, foundItemId ->
                    navController.navigate(Routes.matchReview(lostItemId, foundItemId))
                },
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = Routes.POST_ITEM,
            arguments = listOf(navArgument("kind") { type = NavType.StringType }),
        ) { backStackEntry ->
            PostItemScreen(
                createKind = backStackEntry.arguments?.getString("kind"),
                editItemId = null,
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.EDIT_ITEM,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
        ) { backStackEntry ->
            PostItemScreen(
                createKind = null,
                editItemId = backStackEntry.arguments?.getString("itemId"),
                onNavigateBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.MATCH_REVIEW,
            arguments = listOf(
                navArgument("lostItemId") { type = NavType.StringType },
                navArgument("foundItemId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            MatchReviewScreen(
                lostItemId = backStackEntry.arguments?.getString("lostItemId").orEmpty(),
                foundItemId = backStackEntry.arguments?.getString("foundItemId").orEmpty(),
                onNavigateBack = { navController.popBackStack() },
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
