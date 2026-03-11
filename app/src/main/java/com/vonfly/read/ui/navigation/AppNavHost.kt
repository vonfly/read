package com.vonfly.read.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vonfly.read.ui.screen.bookdetail.BookDetailScreen
import com.vonfly.read.ui.screen.booklist.BookListScreen
import com.vonfly.read.ui.screen.bookstore.StoreScreen

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
                }
            )
        }
    }
}
