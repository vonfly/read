package com.vonfly.read.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable object HomeRoute

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()  // 与 infrastructure.md 模板签名一致
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            // 最小首页占位：确认编译通过、模拟器可见
            // 开发书架功能后替换为 BookListScreen(...)
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Hello, Reading App 📚")
            }
        }
    }
}
