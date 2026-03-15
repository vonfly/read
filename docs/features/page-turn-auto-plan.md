# 翻页效果与自动翻页功能实现计划

## 状态概览
- 创建时间：2026-03-15
- 完成时间：2026-03-15
- 进度：7/7 (100%) ✅ **已完成**

---

## Context

阅读器的翻页效果和自动翻页功能实现。

### 目标
1. **翻页效果**：滑动（SLIDE）、覆盖（COVER）、仿真（REAL，暂不实现）
2. **自动翻页**：**滚动模式**（从下往上滚动），点击暂停并显示配置面板

### 自动滚动交互流程 ✅
1. 更多面板开启自动翻页 → 隐藏所有面板，开始滚动
2. 内容从下往上连续滚动（基础速度 60px/s × 速度倍数）
3. 点击屏幕 → 停止滚动 + 显示配置面板
4. 点击面板外区域 → 隐藏面板 + 恢复滚动
5. 点击"退出自动翻页"按钮 → 退出自动翻页模式

---

## 第一批：翻页组件（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/PageTurnSlide.kt` | 滑动翻页组件 |

---

## 第二批：覆盖翻页组件（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/PageTurnCover.kt` | 覆盖翻页组件 |

---

## 第三批：ReaderContent 集成（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 根据 pageTurnMode 条件渲染翻页组件 |
| ✅ | `ui/screen/reader/ReaderScreen.kt` | 添加 onPageChange 回调 |

---

## 第四批：自动翻页配置面板（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/AutoPageConfigPanel.kt` | 自动翻页配置面板（速度选择 + 退出按钮） |

---

## 第五批：数据层扩展（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/ReaderSettings.kt` | 添加 autoPageSpeed 字段 |
| ✅ | `domain/repository/ReaderPreferencesRepository.kt` | 添加更新方法接口 |
| ✅ | `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` | 实现 DataStore 读写 |
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 添加自动翻页相关方法 |

---

## 第六批：自动滚动组件（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/AutoScrollReader.kt` | 自动滚动阅读组件 |

### AutoScrollReader 实现要点

```kotlin
@Composable
fun AutoScrollReader(
    pages: ImmutableList<PageContent>,
    currentPageIndex: Int,
    chapterTitle: String,
    speed: Float,           // 1.0f, 1.5f, 2.0f, 3.0f
    isPaused: Boolean,
    currentColorScheme: ReaderColorScheme,
    onPageChange: (Int) -> Unit,
    onPause: () -> Unit,
    onScrollToBottom: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var renderedPageIndex by remember { mutableIntStateOf(currentPageIndex) }
    var isLayoutComplete by remember { mutableStateOf(false) }

    // 自动滚动逻辑
    LaunchedEffect(speed, isPaused, renderedPageIndex, isLayoutComplete) {
        if (!isPaused && pages.isNotEmpty() && isLayoutComplete) {
            val pixelsPerSecond = 60f * speed  // 基础速度 60px/s
            val frameDelayMs = 16L
            val scrollPerFrame = pixelsPerSecond / 60f
            var totalScrolled = 0f

            while (true) {
                // 底部检测（累计滚动超过50px才判断，避免误判）
                val isAtBottom = checkIsAtBottom(listState, totalScrolled, 50f)

                if (isAtBottom) {
                    if (renderedPageIndex < pages.size - 1) {
                        // 切换到下一页
                        renderedPageIndex++
                        onPageChange(renderedPageIndex)
                        listState.scrollToItem(0)
                        totalScrolled = 0f
                        delay(100)
                    } else {
                        onScrollToBottom()
                        break
                    }
                } else {
                    // 继续滚动
                    scope.launch {
                        listState.scroll { scrollBy(-scrollPerFrame) }
                    }
                    totalScrolled += scrollPerFrame
                    delay(frameDelayMs)
                }
            }
        }
    }

    // 点击暂停（仅在非暂停状态时响应）
    Box(modifier = modifier
        .fillMaxSize()
        .background(currentColorScheme.background)
        .then(
            if (!isPaused) Modifier.pointerInput(Unit) { detectTapGestures { onPause() } }
            else Modifier
        )
    ) {
        LazyColumn(state = listState, ...) { ... }
    }
}
```

### 关键点

1. **滚动方向**：`scrollBy(-scrollPerFrame)` 负数表示向上滚动
2. **基础速度**：60px/s，乘以速度倍数
3. **布局检测**：等待 50ms 确保 LazyColumn 布局完成
4. **底部检测**：累计滚动超过 50px 才判断，避免初始状态误判
5. **点击暂停**：仅在非暂停状态时响应点击

---

## 第七批：ReaderContent 集成自动滚动（已完成 ✅）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 添加自动滚动模式渲染 + 透明遮罩层 |
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 添加 autoPagePaused 字段 + 暂停/恢复方法 |

### ReaderContent 修改

```kotlin
// ReaderContent.kt
if (uiState.readerSettings.autoPageEnabled) {
    Box(modifier = Modifier.fillMaxSize()) {
        AutoScrollReader(
            pages = uiState.pages,
            currentPageIndex = uiState.currentPageIndex,
            chapterTitle = uiState.chapterTitle,
            speed = uiState.readerSettings.autoPageSpeed,
            isPaused = uiState.autoPagePaused,
            currentColorScheme = uiState.readerSettings.colorScheme,
            onPageChange = onPageChange,
            onPause = { setAutoPagePaused(true) },
            onScrollToBottom = onScrollToBottom,
            modifier = Modifier.fillMaxSize()
        )

        if (uiState.autoPagePaused) {
            // 透明遮罩层 - 点击恢复滚动
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            down.consume()
                            val up = waitForUpOrCancellation()
                            up?.consume()
                            setAutoPagePaused(false)
                        }
                    }
            )

            // 配置面板（阻止点击穿透）
            AutoPageConfigPanel(
                autoPageEnabled = true,
                autoPageSpeed = uiState.readerSettings.autoPageSpeed,
                currentColorScheme = uiState.readerSettings.colorScheme,
                onAutoPageEnabledChange = onAutoPageEnabledChange,
                onAutoPageSpeedChange = onAutoPageSpeedChange,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
```

### ViewModel 方法

```kotlin
// ReaderViewModel.kt
data class ReaderUiState(
    // ...existing fields
    val autoPagePaused: Boolean = false  // 自动滚动是否暂停
)

fun setAutoPagePaused(paused: Boolean) {
    _uiState.update { it.copy(autoPagePaused = paused) }
}

fun onAutoPageEnabledChange(enabled: Boolean) {
    viewModelScope.launch {
        readerPreferencesRepository.updateAutoPageEnabled(enabled)
        if (enabled) {
            // 开启时隐藏所有面板，开始滚动
            _uiState.update { it.copy(
                isControlsVisible = false,
                visiblePanel = null,
                autoPagePaused = false
            )}
            stopAutoPage()
        } else {
            // 关闭时停止滚动
            stopAutoPage()
            _uiState.update { it.copy(autoPagePaused = false) }
        }
    }
}

fun onScrollToBottom() {
    viewModelScope.launch {
        _event.send(ReaderUiEvent.ShowSnackbar("已到达最后一页"))
        readerPreferencesRepository.updateAutoPageEnabled(false)
        _uiState.update { it.copy(autoPagePaused = false) }
    }
}
```

---

## Verification ✅

### 编译验证
```bash
./gradlew assembleDebug  # ✅ BUILD SUCCESSFUL
```

### 翻页效果验证
1. ✅ 滑动翻页：左右滑动翻页
2. ✅ 覆盖翻页：新页面从右侧覆盖
3. ✅ 点击区域翻页

### 自动滚动验证
1. ✅ 开启自动翻页后，面板隐藏，开始滚动
2. ✅ 内容从下往上连续滚动
3. ✅ 点击屏幕停止滚动，显示配置面板
4. ✅ 再次点击面板外区域，隐藏面板，恢复滚动
5. ✅ 修改速度，滚动速度变化
6. ✅ 点击"退出自动翻页"按钮，退出自动翻页模式
7. ✅ 滚动到最后一页底部时显示"已到达最后一页"提示

---

## 不在本期范围内

- 仿真翻页（REAL）效果
- 间隔时间配置（改用速度控制）
- 章节自动跳转
- 滚动时的页面指示器
