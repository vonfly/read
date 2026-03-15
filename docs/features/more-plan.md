# 更多面板功能实现计划

## 状态概览
- 创建时间：2026-03-15
- 进度：0/8 (0%)

---

## Context

阅读器底部控制栏的"更多"设置功能，通过点击"更多"按钮切换到更多面板，支持翻页方式选择和自动翻页开关。

### 现状
- ✅ `ReaderPanel.MORE` 枚举值已存在
- ✅ `ReaderContent.kt` 中已有 `MORE` 分支（内容为 TODO）
- ✅ `BottomControlButtons` 组件支持更多按钮高亮
- ❌ `ReaderSettings` 缺少翻页方式和自动翻页字段
- ❌ `ReaderPreferencesRepository` 缺少相关方法
- ❌ `ReaderViewModel.onMoreClick()` 当前只显示 Toast
- ❌ `MoreBottomPanel` 组件未实现

### 目标
实现完整的更多设置面板，支持：
1. 翻页方式选择（仿真/覆盖/滑动）
2. 自动翻页开关
3. 设置持久化到 DataStore
4. 面板切换交互

---

## 第一批：Domain 层扩展（可并行）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ⬜ | `domain/model/PageTurnMode.kt` | **新建**，翻页方式枚举（SLIDE/COVER/REAL） |
| ⬜ | `domain/model/ReaderSettings.kt` | 新增 `pageTurnMode` 和 `autoPageEnabled` 字段 |
| ⬜ | `domain/repository/ReaderPreferencesRepository.kt` | 新增 `updatePageTurnMode()` 和 `updateAutoPageEnabled()` 接口方法 |

---

## 第二批：Data 层实现（依赖第一批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ⬜ | `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` | 1. 新增 `PAGE_TURN_MODE` 和 `AUTO_PAGE_ENABLED` keys<br>2. `observeSettings()` 中读取新字段<br>3. 实现 `updatePageTurnMode()` 和 `updateAutoPageEnabled()` 方法 |

---

## 第三批：ViewModel 层（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ⬜ | `ui/screen/reader/ReaderViewModel.kt` | 1. 修改 `onMoreClick()` 切换面板（参考 `onFontClick`）<br>2. 新增 `onPageTurnModeChange(mode: PageTurnMode)`<br>3. 新增 `onAutoPageEnabledChange(enabled: Boolean)` |

---

## 第四批：UI 组件层（依赖第三批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ⬜ | `ui/screen/reader/components/MoreBottomPanel.kt` | **新建**，更多设置面板组件 |

### MoreBottomPanel 组件结构（参考 FontBottomPanel）

```
MoreBottomPanel (238dp, 顶部圆角 16dp, 阴影向上)
├── PageTurnSection (90dp)
│   ├── 标题 "翻页方式" (14sp, Medium)
│   └── SegmentedControl (40dp, 3选项: 仿真/覆盖/滑动)
│       ├── Option1 (36dp, "仿真")
│       ├── Option2 (36dp, "覆盖")
│       └── Option3 (36dp, "滑动")
│
├── AutoPageSection (66dp)
│   ├── 标题 "自动翻页" (14sp, Medium)
│   └── Toggle (51×31dp, 开关)
│
└── BottomControlButtons (66dp, 复用组件, activeButton = MORE)
```

### 组件规格（来自 more-spec.md）

**PageTurnSection 翻页选项：**
| 属性 | 值 |
|------|-----|
| 选项高度 | 36dp |
| 选项圆角 | 8dp |
| 选中背景 | 亮色: #FFFFFF / 深色: #5A5A5A |
| 未选中背景 | 亮色: #F5F5F5 / 深色: #3A3A3A |
| 选中阴影 | blur=2, color=#00000020, offset=(0, 1) |

**AutoPageSection 开关：**
| 属性 | 值 |
|------|-----|
| 宽度 | 51dp |
| 高度 | 31dp |
| 圆角 | 16dp |
| 轨道颜色（关闭） | 亮色: #E5E5EA / 深色: #3A3A3A |
| 轨道颜色（开启） | Accent (#FF6B4A) |
| 滑块尺寸 | 27×27dp |
| 滑块颜色 | #FFFFFF |

---

## 第五批：UI 集成层（依赖第四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ⬜ | `ui/screen/reader/ReaderContent.kt` | 在 `when (visiblePanel)` 的 `MORE` 分支调用 `MoreBottomPanel` |

---

## 复用组件

| 组件 | 文件 | 说明 |
|------|------|------|
| `BottomControlButtons` | `ReaderBottomBar.kt` | 底部控制按钮（目录/亮度/字体/更多），已提取为可复用组件 |
| `SegmentedControl` | `FontBottomPanel.kt` | 分段控制器，复用字体面板的实现（主题适配已完成） |
| 阴影实现 | `BrightnessBottomPanel.kt` | `drawBehind` + `setShadowLayer(dy = -4f)` |

---

## 关键实现细节

### 1. PageTurnMode.kt 新建

```kotlin
package com.vonfly.read.domain.model

enum class PageTurnMode {
    REAL,    // 仿真翻页
    COVER,   // 覆盖翻页
    SLIDE    // 滑动翻页
}
```

### 2. ReaderSettings.kt 修改

```kotlin
data class ReaderSettings(
    val fontSize: TextUnit = 18.sp,
    val lineHeight: Float = 1.8f,
    val letterSpacing: Float = 0f,
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default,
    val brightness: Float = 1.0f,
    val pageTurnMode: PageTurnMode = PageTurnMode.SLIDE,      // 新增
    val autoPageEnabled: Boolean = false                       // 新增
)
```

### 3. ReaderPreferencesRepository.kt 修改

```kotlin
// 新增方法
suspend fun updatePageTurnMode(mode: PageTurnMode)
suspend fun updateAutoPageEnabled(enabled: Boolean)
```

### 4. ReaderPreferencesRepositoryImpl.kt 修改

```kotlin
companion object {
    // ... 现有 keys
    private val PAGE_TURN_MODE = stringPreferencesKey("reader_page_turn_mode")
    private val AUTO_PAGE_ENABLED = booleanPreferencesKey("reader_auto_page_enabled")

    private const val DEFAULT_PAGE_TURN_MODE = "SLIDE"
    private const val DEFAULT_AUTO_PAGE_ENABLED = false
}

// observeSettings() 中新增
val pageTurnMode = PageTurnMode.valueOf(
    prefs[PAGE_TURN_MODE] ?: DEFAULT_PAGE_TURN_MODE
)
val autoPageEnabled = prefs[AUTO_PAGE_ENABLED] ?: DEFAULT_AUTO_PAGE_ENABLED

// 新增方法
override suspend fun updatePageTurnMode(mode: PageTurnMode) {
    dataStore.edit { it[PAGE_TURN_MODE] = mode.name }
}

override suspend fun updateAutoPageEnabled(enabled: Boolean) {
    dataStore.edit { it[AUTO_PAGE_ENABLED] = enabled }
}
```

### 5. ReaderViewModel.kt 修改

```kotlin
// 修改 onMoreClick()（参考 onFontClick）
fun onMoreClick() {
    _uiState.update { state ->
        state.copy(
            visiblePanel = if (state.visiblePanel == ReaderPanel.MORE) null else ReaderPanel.MORE
        )
    }
}

// 新增方法
fun onPageTurnModeChange(mode: PageTurnMode) {
    viewModelScope.launch {
        readerPreferencesRepository.updatePageTurnMode(mode)
    }
}

fun onAutoPageEnabledChange(enabled: Boolean) {
    viewModelScope.launch {
        readerPreferencesRepository.updateAutoPageEnabled(enabled)
    }
}
```

### 6. MoreBottomPanel.kt 新建

```kotlin
@Composable
fun MoreBottomPanel(
    pageTurnMode: PageTurnMode,
    autoPageEnabled: Boolean,
    currentColorScheme: ReaderColorScheme,
    onPageTurnModeChange: (PageTurnMode) -> Unit,
    onAutoPageEnabledChange: (Boolean) -> Unit,
    onCatalogClick: () -> Unit,
    onFontClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 面板容器：238dp, 顶部圆角 16dp, 阴影向上
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(238.dp)
            .drawBehind { /* 阴影实现，参考 BrightnessBottomPanel */ }
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(currentColorScheme.panelBackground)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            // PageTurnSection (90dp)
            // AutoPageSection (66dp)
            // BottomControlButtons (66dp)
        }
    }
}
```

### 7. ReaderContent.kt 修改

```kotlin
// 修改 MORE 分支
ReaderPanel.MORE -> MoreBottomPanel(
    pageTurnMode = uiState.readerSettings.pageTurnMode,
    autoPageEnabled = uiState.readerSettings.autoPageEnabled,
    currentColorScheme = uiState.readerSettings.colorScheme,
    onPageTurnModeChange = viewModel::onPageTurnModeChange,
    onAutoPageEnabledChange = viewModel::onAutoPageEnabledChange,
    onCatalogClick = viewModel::onCatalogClick,
    onFontClick = viewModel::onFontClick,
    onBrightnessClick = viewModel::onBrightnessClick,
    onMoreClick = viewModel::onMoreClick
)
```

---

## 数据规格

| 设置项 | 选项值 | 默认值 | 说明 |
|--------|--------|--------|------|
| 翻页方式 | REAL / COVER / SLIDE | SLIDE | 仿真/覆盖/滑动 |
| 自动翻页 | true / false | false | 开关 |

---

## Verification

1. **编译验证**：`./gradlew assembleDebug` 通过
2. **UI 验收**：
   - 点击"更多"按钮，底部面板切换为更多面板，TopBar 隐藏
   - 面板高度 238dp，顶部圆角 16dp
   - 点击翻页方式选项切换翻页模式
   - 切换自动翻页开关
   - 更多按钮高亮显示（Accent 色）
3. **数据验证**：
   - 退出阅读器后重新进入，设置保持
   - DataStore 中保存 pageTurnMode 和 autoPageEnabled 值
4. **交互验证**：
   - 再次点击"更多"按钮，切换回默认控制栏
   - 点击其他按钮（目录/亮度/字体）正常触发对应功能
5. **主题适配验证**：
   - 6 种主题下面板颜色正确
   - SegmentedControl 和开关颜色正确

---

## 不在本期范围内
- 翻页速度设置
- 自动翻页间隔时间设置
- 音效开关
- 屏幕方向锁定
- 全屏模式
- 翻页效果实际应用到阅读器（本期只做设置持久化）
