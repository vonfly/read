# 项目初始化指南

> 按此文件从零初始化项目，完成后可在模拟器看到第一个画面。
> 完成初始化后，功能开发参考 `docs/workflow.md`。

---

## 第一步：Android Studio 新建项目

1. File → New Project → **Empty Activity**（不要选带 Bottom Navigation 的模板）
2. Language: **Kotlin**，Minimum SDK: **API 26**，Build configuration language: **Kotlin DSL**
3. 创建完成后删除自动生成的 `ui/theme/` 目录（后面重新建规范的版本）

---

## 第二步：完整 `libs.versions.toml`

将 `gradle/libs.versions.toml` 替换为以下内容：

```toml
[versions]
agp                           = "8.7.3"
kotlin                        = "2.0.21"
compose-bom                   = "2026.02.01"
hilt                          = "2.52"
navigation                    = "2.8.9"
room                          = "2.7.1"
datastore                     = "1.1.1"
retrofit                      = "2.11.0"
okhttp                        = "4.12.0"
coil                          = "2.7.0"
coroutines                    = "1.9.0"
lifecycle                     = "2.8.7"
kotlinx-serialization         = "1.7.3"
kotlinx-collections-immutable = "0.3.8"
paging                        = "3.4.1"

[libraries]
# Compose BOM（统一管理所有 Compose 子库版本）
compose-bom                = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui                 = { group = "androidx.compose.ui", name = "ui" }
compose-ui-tooling         = { group = "androidx.compose.ui", name = "ui-tooling" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3          = { group = "androidx.compose.material3", name = "material3" }
compose-activity           = { group = "androidx.activity", name = "activity-compose", version = "1.9.3" }

# Hilt
hilt-android               = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler              = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose    = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# Navigation
navigation-compose         = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }

# Lifecycle
lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
lifecycle-runtime-compose   = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycle" }

# Room
room-runtime               = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx                   = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler              = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# DataStore
datastore-preferences      = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }

# Network
retrofit                   = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-kotlin-serialization = { group = "com.jakewharton.retrofit", name = "retrofit2-kotlinx-serialization-converter", version = "1.0.0" }
okhttp                     = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
okhttp-logging             = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }

# Coroutines
coroutines-android         = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

# Serialization
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

# Collections
kotlinx-collections-immutable = { group = "org.jetbrains.kotlinx", name = "kotlinx-collections-immutable", version.ref = "kotlinx-collections-immutable" }

# Image
coil-compose               = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

# Paging
paging-runtime             = { group = "androidx.paging", name = "paging-runtime", version.ref = "paging" }
paging-compose             = { group = "androidx.paging", name = "paging-compose", version.ref = "paging" }

# Core
core-ktx                   = { group = "androidx.core", name = "core-ktx", version = "1.15.0" }

[plugins]
android-application        = { id = "com.android.application", version.ref = "agp" }
android-library            = { id = "com.android.library", version.ref = "agp" }
kotlin-android             = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose             = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization       = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
hilt                       = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp                        = { id = "com.google.devtools.ksp", version = "2.0.21-1.0.28" }
room                       = { id = "androidx.room", version.ref = "room" }
```

---

## 第三步：`app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)        // Kotlin 2.0+ 必须，替代旧的 composeCompiler 配置
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.yourcompany.yourapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yourcompany.yourapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    // Compose 编译器必须显式启用
    buildFeatures { compose = true }

    room {
        schemaDirectory("$projectDir/schemas")  // Room 插件方式，替代旧的 annotationProcessorOptions
    }
}

dependencies {
    // Compose BOM（必须放在最前面）
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Navigation
    implementation(libs.navigation.compose)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Coroutines
    implementation(libs.coroutines.android)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Collections
    implementation(libs.kotlinx.collections.immutable)

    // Image
    implementation(libs.coil.compose)

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Core
    implementation(libs.core.ktx)
}
```

---

## 第四步：`build.gradle.kts`（项目根目录）

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}
```

---

## 第五步：最小可运行代码（4 个文件）

### `MyApp.kt`（Application 类）

```kotlin
package com.yourcompany.yourapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()
```

在 `AndroidManifest.xml` 的 `<application>` 标签加上：
```xml
android:name=".MyApp"
```

### `MainActivity.kt`

```kotlin
package com.yourcompany.yourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yourcompany.yourapp.ui.navigation.AppNavHost
import com.yourcompany.yourapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavHost()
            }
        }
    }
}
```

### `ui/theme/AppTheme.kt`

```kotlin
package com.yourcompany.yourapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
        content = content
    )
}
```

### `ui/navigation/AppNavHost.kt`

```kotlin
package com.yourcompany.yourapp.ui.navigation

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
```

---

## 第六步：验证

在 Android Studio 中：
1. **Build → Make Project**（确认无编译错误）
2. **Run → 选择模拟器**（API 26+）
3. 看到 "Hello, Reading App 📚" 字样即初始化成功

如果编译失败，最常见的原因：
1. `AndroidManifest.xml` 忘记加 `android:name=".MyApp"`
2. `ksp` 版本与 `kotlin` 版本不匹配（`ksp = "2.0.21-1.0.28"` 对应 `kotlin = "2.0.21"`）
3. `buildFeatures { compose = true }` 被误删

---

## 初始化成功后的下一步

参考 `docs/workflow.md` 开始第一个功能模块的开发流程：
先写 `docs/features/booklist-spec.md`，再用 Plan 模式拆分任务，然后逐文件实现。
