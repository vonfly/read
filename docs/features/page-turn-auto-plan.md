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
2. **自动翻页**：**整章滚动模式**（所有段落合并，从下往上连续滚动）

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
| ✅ | `ui/screen/reader/components/AutoScrollReader.kt` | 自动滚动阅读组件（整章滚动模式） |

### AutoScrollReader 实现要点（整章滚动模式）

```kotlin
/**
 * 自动滚动阅读组件（整章滚动模式）
 *
 * 将所有页面的段落合并成一个列表，从下往上连续滚动。
 *
 * 交互流程：
 * 1. 开启自动翻页后，隐藏所有面板，开始滚动
 * 2. 点击屏幕 → 停止滚动 + 触发 onPause 回调
 * 3. 外部控制 isPaused 决定是否继续滚动
 */
@Composable
fun AutoScrollReader(
    pages: ImmutableList<PageContent>,
    chapterTitle: String,
    speed: Float,
    isPaused: Boolean,
    currentColorScheme: ReaderColorScheme,
    onScrollToBottom: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    // 保留参数签名兼容性，但不再使用
    currentPageIndex: Int = 0,
    onPageChange: (Int) -> Unit = {}
) {
    val settings = LocalReaderSettings.current
    val listState = rememberLazyListState()

    // 合并所有段落到一个列表（整章滚动）
    val allParagraphs: List<ParagraphItem> = remember(pages, chapterTitle) {
        buildList {
            if (chapterTitle.isNotEmpty()) {
                add(ParagraphItem.Title(chapterTitle))
            }
            pages.forEach { page ->
                page.paragraphs.forEach { paragraph ->
                    add(ParagraphItem.Text(paragraph))
                }
            }
        }
    }

    // 布局完成状态
    var isLayoutComplete by remember { mutableStateOf(false) }
    // 累计滚动距离（用于避免误判底部）
    var totalScrolled by remember { mutableFloatStateOf(0f) }

    // 监听布局完成
    LaunchedEffect(allParagraphs) {
        delay(50)
        isLayoutComplete = true
    }

    // 自动滚动逻辑
    LaunchedEffect(speed, isPaused, isLayoutComplete, allParagraphs.isNotEmpty()) {
        if (!isPaused && allParagraphs.isNotEmpty() && isLayoutComplete) {
            val pixelsPerSecond = 60f * speed  // 基础速度 60px/s
            val frameDelayMs = 16L
            val scrollPerFrame = pixelsPerSecond / 60f

            while (isActive) {
                val layoutInfo = listState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                val isAtBottom = if (lastVisibleItem != null && layoutInfo.totalItemsCount > 0) {
                    val isLastItem = lastVisibleItem.index == layoutInfo.totalItemsCount - 1
                    val viewportEnd = layoutInfo.viewportEndOffset
                    val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
                    // 最后一项完全可见，且已经滚动了一定距离（避免误判）
                    isLastItem && itemBottom <= viewportEnd && totalScrolled > 50f
                } else {
                    false
                }

                if (isAtBottom) {
                    onScrollToBottom()
                    break
                } else {
                    // 继续向下滚动（正值 = 内容向上移动）
                    listState.scroll {
                        scrollBy(scrollPerFrame)
                    }
                    totalScrolled += scrollPerFrame
                    delay(frameDelayMs)
                }
            }
        }
    }

    // 重置滚动距离当暂停状态改变
    LaunchedEffect(isPaused) {
        if (!isPaused) {
            totalScrolled = 0f
        }
    }

    // 点击暂停（仅在非暂停状态时响应）
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentColorScheme.background)
            .then(
                if (!isPaused) Modifier.pointerInput(Unit) { detectTapGestures { onPause() } }
                else Modifier
            )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 28.dp,
                bottom = 72.dp
            )
        ) {
            items(count = allParagraphs.size, key = { "paragraph_$it" }) { index ->
                when (val item = allParagraphs[index]) {
                    is ParagraphItem.Title -> {
                        Text(
                            text = item.text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ForegroundSecondary
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    is ParagraphItem.Text -> {
                        Text(
                            text = item.text,
                            fontSize = settings.fontSize,
                            lineHeight = settings.fontSize * settings.lineHeight,
                            letterSpacing = settings.letterSpacing.sp,
                            color = currentColorScheme.text
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

// 段落项类型
private sealed class ParagraphItem {
    data class Title(val text: String) : ParagraphItem()
    data class Text(val text: String) : ParagraphItem()
}
```

### 关键点

1. **整章滚动**：所有页面的段落合并成一个列表，连续滚动
2. **滚动方向**：`scrollBy(scrollPerFrame)` **正值**表示向下滚动 = 内容向上移动
3. **基础速度**：60px/s，乘以速度倍数
4. **布局检测**：等待 50ms 确保 LazyColumn 布局完成
5. **底部检测**：使用 `viewportEndOffset` + 累计滚动超过 50px 才判断，避免初始状态误判
6. **点击暂停**：仅在非暂停状态时响应点击

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
            chapterTitle = uiState.chapterTitle,
            speed = uiState.readerSettings.autoPageSpeed,
            isPaused = uiState.autoPagePaused,
            currentColorScheme = uiState.readerSettings.colorScheme,
            onScrollToBottom = onScrollToBottom,
            onPause = { setAutoPagePaused(true) },
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
                onAutoPageEnabledChange = onAutoPageEnabledChange,
                onAutoPageSpeedChange = onAutoPageSpeedChange,
                currentColorScheme = uiState.readerSettings.colorScheme,
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
        } else {
            // 关闭时停止滚动
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
2. ✅ 内容从下往上连续滚动（整章滚动模式）
3. ✅ 点击屏幕停止滚动，显示配置面板
4. ✅ 再次点击面板外区域，隐藏面板，恢复滚动
5. ✅ 修改速度，滚动速度变化
6. ✅ 点击"退出自动翻页"按钮，退出自动翻页模式
7. ✅ 滚动到章节底部时显示"已到达最后一页"提示

---

## 不在本期范围内

- 仿真翻页（REAL）效果
- 间隔时间配置（改用速度控制）
- 章节自动跳转
- 滚动时的页面指示器
