package com.vonfly.read.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vonfly.read.ui.screen.bookdetail.BookDetailScreen
import com.vonfly.read.ui.screen.booklist.BookListScreen
import com.vonfly.read.ui.screen.bookmark.BookmarkScreen
import com.vonfly.read.ui.screen.bookstore.StoreScreen
import com.vonfly.read.ui.screen.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BookListRoute
    ) {
        composable<BookListRoute> {
            BookListScreen(
                onNavigateToBookDetail = { bookId ->
                    navController.navigate(BookDetailRoute(bookId))
                },
                onNavigateToStore = {
                    navController.navigate(StoreRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(ProfileRoute) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<BookDetailRoute> {
            val args = it.toRoute<BookDetailRoute>()
            BookDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToContents = { bookId ->
                    // TODO: 后续实现目录页面后启用
                }
            )
        }
        composable<StoreRoute> {
            StoreScreen(
                onNavigateToBookDetail = { bookId ->
                    navController.navigate(BookDetailRoute(bookId))
                },
                onNavigateToBookList = {
                    navController.popBackStack(BookListRoute, inclusive = false)
                },
                onNavigateToProfile = {
                    navController.navigate(ProfileRoute) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<ProfileRoute> {
            ProfileScreen(
                onNavigateToBookList = {
                    navController.popBackStack(BookListRoute, inclusive = false)
                },
                onNavigateToStore = {
                    navController.navigate(StoreRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateToBookmarks = {
                    navController.navigate(BookmarkRoute)
                }
            )
        }
        composable<BookmarkRoute> {
            BookmarkScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
