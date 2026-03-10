package com.vonfly.read.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vonfly.read.ui.screen.booklist.BookListScreen

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
                onNavigateToReader = { bookId ->
                    // TODO: 后续实现阅读器页面后启用
                    // navController.navigate(ReaderRoute(bookId))
                }
            )
        }
        // TODO: 阅读器页面
        // composable<ReaderRoute> {
        //     val args = it.toRoute<ReaderRoute>()
        //     ReaderScreen(bookId = args.bookId, onNavigateBack = { navController.popBackStack() })
        // }
    }
}
