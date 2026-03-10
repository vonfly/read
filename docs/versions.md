# 依赖配置规范

> 版本号以项目 `libs.versions.toml`（通常在 `gradle/` 目录下）为唯一真实来源，
> 此文件只记录配置规范，不记录具体版本号。

---

## 新增依赖前必须做的事

1. **先检查 `libs.versions.toml` 是否已存在该库**，避免重复引入不同版本
2. **去官方渠道查最新稳定版本号**，禁止凭记忆填写：
   - AndroidX 库（Room / Navigation / Lifecycle 等）→ [developer.android.com/jetpack/androidx/releases](https://developer.android.com/jetpack/androidx/releases)
   - Kotlin / KSP / Coroutines → [github.com/JetBrains/kotlin/releases](https://github.com/JetBrains/kotlin/releases)
   - Hilt → [github.com/google/dagger/releases](https://github.com/google/dagger/releases)
   - 第三方库（Retrofit / Coil / OkHttp 等）→ 对应 GitHub releases 页

---

## 新增依赖格式（libs.versions.toml）

正确写法是所有条目分别归属到各自的 section，**同一个 section 只声明一次**：

```toml
[versions]
coil = "2.7.0"
retrofit = "2.11.0"
# ... 所有版本号都在这一个 [versions] 块里

[libraries]
# 非 Compose 库：必须在 [versions] 中有对应版本引用
coil-compose         = { group = "io.coil-kt",          name = "coil-compose",  version.ref = "coil" }
retrofit             = { group = "com.squareup.retrofit2", name = "retrofit",    version.ref = "retrofit" }

# Compose 子库：不写版本号，由 BOM 统一管理（BOM 已在 build.gradle.kts 中引入）
compose-material3    = { group = "androidx.compose.material3", name = "material3" }
compose-ui           = { group = "androidx.compose.ui",        name = "ui" }

# 所有 libraries 条目都在这一个 [libraries] 块里，不要重复声明 [libraries]

[plugins]
some-plugin = { id = "com.example.plugin", version.ref = "some-version" }
```

⚠️ **`[versions]`、`[libraries]`、`[plugins]` 每个 section 在文件中只能出现一次**，
toml 不允许重复声明同名 section，否则 Gradle sync 会报错。

---

## Kotlin 2.0+ Compose Compiler 配置

Kotlin 2.0 起，Compose Compiler 内置为 Kotlin 编译器插件，**不再是独立依赖**。

**`libs.versions.toml` 的 `[plugins]` 中声明：**
```toml
[plugins]
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

**根目录 `build.gradle.kts` 中注册（加 `apply false`）：**
```kotlin
plugins {
    alias(libs.plugins.kotlin.compose) apply false
}
```

**`app/build.gradle.kts` 中启用：**
```kotlin
plugins {
    alias(libs.plugins.kotlin.compose)  // 不加 apply false
}
```

⚠️ 若项目存在 `kotlinCompilerExtensionVersion` 设置 → 配置有误，必须删除。

---

## KSP 版本必须与 Kotlin 版本严格对应

KSP 的版本号格式是 `{kotlinVersion}-{kspVersion}`，例如：

```toml
[versions]
kotlin = "2.0.21"
ksp    = "2.0.21-1.0.28"  # 前缀必须与 kotlin 版本完全一致
```

**查找对应版本：** [github.com/google/ksp/releases](https://github.com/google/ksp/releases)
页面中每个 release 的标题格式即为 `kotlinVersion-kspVersion`，找到与你的 kotlin 版本匹配的最新条目。

Kotlin 版本升级时，KSP 版本必须同步更新，两者不匹配会导致编译失败。
