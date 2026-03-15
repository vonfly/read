# 翻页效果与自动翻页功能 Spec

## 要解决的问题

阅读器的翻页效果和自动翻页功能：

### 翻页模式
- **横滑（SLIDE）**：左右滑动翻页，页面跟随手指移动 ✅
- **覆盖（COVER）**：新页面从右侧覆盖旧页面 ✅
- **仿真（REAL）**：仿真翻页效果（本期暂不实现）

### 自动翻页（整章滚动模式）✅

**交互流程**：
1. 更多面板开启"自动翻页"开关
2. 开启后**隐藏所有面板**，进入自动滚动模式
3. 内容**从下往上自动滚动**（整章连续滚动，不是逐页切换）
4. 点击屏幕任意位置 → **停止滚动** + 显示配置面板
5. 再次点击屏幕（面板外区域）→ **隐藏面板** + **恢复滚动**
6. 点击"退出自动翻页"按钮 → **关闭自动翻页模式**

**效果**：
- 所有页面的段落合并成一个列表，连续向上滚动
- 滚动到章节底部时触发回调（可跳转下一章）
- 速度可通过配置面板调整（1x, 1.5x, 2x, 3x）

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` 及用户反馈

### 翻页组件

#### PageTurnSlide（滑动翻页）✅

| 属性 | 值 |
|------|-----|
| 实现方式 | HorizontalPager |
| 页面切换动画 | 默认滑动动画 |
| 点击区域 | 左35%上一页，右35%下一页，中间30%显示控制栏 |
| 手势支持 | 左右滑动翻页 |

**双向同步**：
- pagerState.currentPage → 外部 currentPageIndex
- 外部 currentPageIndex → pagerState.animateScrollToPage()

#### PageTurnCover（覆盖翻页）✅

| 属性 | 值 |
|------|-----|
| 实现方式 | AnimatedContent + slideInHorizontally/slideOutHorizontally |
| 动画时长 | 300ms |
| 翻到下一页 | 新页面从右侧滑入覆盖 |
| 翻到上一页 | 新页面从左侧滑入覆盖 |

#### PageTurnReal（仿真翻页）- 暂不实现

| 属性 | 值 |
|------|-----|
| 状态 | 本期跳过 |
| 原因 | 实现复杂，需要自定义 Canvas 绘制 |

### 自动滚动组件（AutoScrollReader）✅

**整章滚动模式**：将所有页面的段落合并成一个列表，从下往上连续滚动。

| 属性 | 值 |
|------|-----|
| 实现方式 | LazyColumn + LaunchedEffect + scroll { scrollBy() } |
| 滚动方向 | 从下往上（内容向上移动） |
| 基础滚动速度 | 60px/s（速度1x） |
| 点击行为 | 停止滚动，触发 onPause 回调 |
| 布局检测 | 等待 50ms 确保布局完成 |
| 底部检测 | 累计滚动超过 50px 后才判断，使用 viewportEndOffset |

**滚动速度计算**：
```
滚动速度 = 60px/s × 速度倍数
1x = 60px/s
1.5x = 90px/s
2x = 120px/s
3x = 180px/s
```

**段落类型**：
- `ParagraphItem.Title`：章节标题（14sp, Medium, ForegroundSecondary）
- `ParagraphItem.Text`：正文段落（使用用户设置的字体大小、行高、字间距）

**内容内边距**：
| 位置 | 值 |
|------|-----|
| start | 20dp |
| end | 20dp |
| top | 28dp |
| bottom | 72dp（留出 Footer 空间） |

### 自动翻页配置面板（AutoPageConfigPanel）✅

| 属性 | 值 |
|------|-----|
| 面板高度 | 150dp |
| 顶部圆角 | 16dp |
| 背景色 | `currentColorScheme.panelBackground` |
| 阴影 | blur=20, color=#00000025 |

**面板结构**：
```
AutoPageConfigPanel (150dp)
├── 速度选择区域 (50dp)
│   ├── 标题 "速度" (14sp, Medium)
│   └── 速度选项 (1x, 1.5x, 2x, 3x)
└── 退出按钮区域 (100dp)
    └── 圆角按钮 "退出自动翻页" (44dp高, 22dp圆角)
```

**速度选项按钮样式**：

| 属性 | 值 |
|------|-----|
| 按钮宽度 | 44dp |
| 按钮高度 | 28dp |
| 按钮圆角 | 6dp |
| 间距 | 8dp |
| 选中背景 | Accent |
| 选中文字 | 白色, SemiBold |
| 未选中背景 | 亮色: #F5F5F5 / 深色: #3A3A3A |
| 未选中文字 | 主题色 60%, Normal |

**退出按钮样式**：

| 属性 | 值 |
|------|-----|
| 按钮高度 | 44dp |
| 按钮圆角 | 22dp（完全圆角） |
| 背景色 | Accent |
| 文字 | "退出自动翻页" |
| 文字颜色 | 白色 |
| 文字大小 | 15sp |
| 文字字重 | SemiBold |

### 主题颜色适配

| 元素 | 亮色主题 | 深色（Night）主题 |
|------|----------|-------------------|
| 面板背景 | `currentColorScheme.panelBackground` | `currentColorScheme.panelBackground` |
| 标题颜色 | `currentColorScheme.text` | `currentColorScheme.text` |
| 选中项背景 | Accent | Accent |
| 未选中项背景 | #F5F5F5 | #3A3A3A |

---

## 验收标准

### 翻页效果验收

#### 横滑翻页（SLIDE）✅
- [x] 左右滑动可以翻页
- [x] 点击左侧区域翻到上一页
- [x] 点击右侧区域翻到下一页
- [x] 点击中间区域显示控制栏
- [x] 滑动过程页面跟随手指移动
- [x] 第一页时不能继续向左滑
- [x] 最后一页时不能继续向右滑

#### 覆盖翻页（COVER）✅
- [x] 翻到下一页时，新页面从右侧覆盖
- [x] 翻到上一页时，新页面从左侧覆盖
- [x] 动画流畅，无卡顿
- [x] 动画时长 300ms
- [x] 点击区域与滑动翻页一致

#### 仿真翻页（REAL）
- [x] 本期跳过，保持简单切换效果

### 自动滚动验收 ✅

- [x] 开启自动翻页后，隐藏所有面板，开始滚动
- [x] 内容从下往上连续滚动（整章滚动，不是逐页切换）
- [x] 滚动速度可配置（1x, 1.5x, 2x, 3x）
- [x] 点击屏幕停止滚动，显示配置面板
- [x] 再次点击屏幕（面板外）隐藏面板，恢复滚动
- [x] 点击"退出自动翻页"按钮，退出自动翻页模式
- [x] 滚动到章节底部时触发 onScrollToBottom 回调
- [x] 到达章节底部时停止并提示"已到达最后一页"
- [x] 速度设置持久化

### 配置面板验收 ✅

- [x] 面板高度 150dp
- [x] 顶部圆角 16dp
- [x] 阴影向上投射
- [x] 背景色随主题变化
- [x] 速度选项点击切换正确
- [x] "退出自动翻页"按钮功能正常
- [x] 面板区域点击不穿透到底层

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 更多面板开启自动翻页 | 隐藏所有面板，开始滚动 |
| 自动滚动时点击屏幕 | 停止滚动，显示配置面板 |
| 配置面板显示时点击面板外区域 | 隐藏面板，恢复滚动 |
| 配置面板点击"退出自动翻页" | 停止滚动，隐藏面板，退出自动翻页模式 |
| 修改速度 | 立即生效，调整滚动速度 |
| 滚动到章节底部 | 停止滚动，显示提示 |

### 状态流转图

```
[正常阅读模式]
      │
      ▼ 更多面板 → 开启自动翻页
[自动滚动模式] ←───────────┐
      │                    │
      ▼ 点击屏幕            │
[暂停滚动 + 显示配置面板]    │
      │                    │
      ├─ 点击面板外区域 ────┘
      │
      └─ 点击"退出自动翻页" → [正常阅读模式]
```

---

## 不在本期范围内

- 仿真翻页效果（PageTurnReal）
- 间隔时间配置（改用速度控制）
- 章节自动跳转
- 滚动时的页面指示器

---

## 技术约束

### 数据来源

| 数据 | 来源 | 说明 |
|------|------|------|
| 翻页模式 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 自动翻页开关 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 自动翻页速度 | `ReaderPreferencesRepository` | DataStore 持久化 |

### 新增组件

| 组件 | 文件 | 职责 |
|------|------|------|
| PageTurnSlide | PageTurnSlide.kt | 滑动翻页组件 ✅ |
| PageTurnCover | PageTurnCover.kt | 覆盖翻页组件 ✅ |
| AutoScrollReader | AutoScrollReader.kt | 自动滚动阅读组件（整章滚动） ✅ |
| AutoPageConfigPanel | AutoPageConfigPanel.kt | 自动翻页配置面板 ✅ |

---

## UI 结构树

### 阅读内容区域（根据模式）

```
ReaderContent
├── if (autoPageEnabled)
│   └── Box
│       ├── AutoScrollReader (LazyColumn 整章滚动)
│       │   └── LazyColumn
│       │       ├── ParagraphItem.Title (章节标题)
│       │       └── ParagraphItem.Text (正文段落) × N
│       └── if (autoPagePaused)
│           ├── 透明遮罩层（点击恢复滚动）
│           └── AutoPageConfigPanel（配置面板）
│
├── else if (isControlsVisible)
│   └── StaticContent (点击隐藏面板)
│       └── ReaderPageLayer
│
└── else (正常翻页模式)
    └── when (pageTurnMode)
        ├── SLIDE → PageTurnSlide
        ├── COVER → PageTurnCover
        └── REAL → SimpleSwitch
```

### 自动翻页配置面板

```
AutoPageConfigPanel (150dp)
├── 速度选择区域 (50dp)
│   └── Row (标题 + 速度选项 1x/1.5x/2x/3x)
└── 退出按钮区域 (100dp)
    └── 圆角宽按钮 "退出自动翻页"
```

---

## 实现对照表

### AutoScrollReader 组件 ✅

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 滚动方式 | 从下往上 | `LazyListState.scroll { scrollBy(scrollPerFrame) }` 正值 | ✅ |
| 滚动速度 | 60-180px/s | `60f * speed / 60f` 每帧 | ✅ |
| 点击暂停 | 停止+显示面板 | `pointerInput` + `onPause` 回调 | ✅ |
| 布局检测 | 等待布局完成 | `isLayoutComplete` + 50ms 延迟 | ✅ |
| 底部检测 | 避免误判 | `totalScrolled > 50f` 阈值 + `viewportEndOffset` | ✅ |
| 段落合并 | 整章滚动 | `buildList` 合并所有段落 | ✅ |

### AutoPageConfigPanel 组件 ✅

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 面板高度 | 150dp | `height(150.dp)` | ✅ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ✅ |
| 阴影方向 | 向上 | `drawBehind` + `dy = -4f` | ✅ |
| 速度选项 | 4个 | `Row` + 4 `Box` | ✅ |
| 退出按钮 | 圆角宽按钮 | `RoundedCornerShape(22.dp)` + Accent 背景 | ✅ |
| 点击阻止 | 不穿透到底层 | `pointerInput` 消费点击 | ✅ |

### ViewModel 自动滚动逻辑 ✅

| 方法 | 功能 | 状态 |
|------|------|------|
| `onAutoPageEnabledChange()` | 开关自动滚动 | ✅ |
| `onAutoPageSpeedChange()` | 更新速度倍数 | ✅ |
| `setAutoPagePaused()` | 暂停/恢复滚动 | ✅ |
| `onScrollToBottom()` | 滚动到底部处理 | ✅ |

### ReaderUiState 扩展 ✅

| 字段 | 类型 | 默认值 | 状态 |
|------|------|--------|------|
| `autoPagePaused` | Boolean | false | ✅ |

---

## 关键实现细节

### AutoScrollReader 滚动逻辑（整章滚动模式）

```kotlin
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

    // 点击暂停（仅在非暂停状态时响应）
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentColorScheme.background)
            .then(
                if (!isPaused) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { onPause() }
                    }
                } else Modifier
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

### 配置面板 + 透明遮罩层

```kotlin
// 在 ReaderContent 中
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
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
```

---

## 注意事项

### scrollBy 方向

在 Compose LazyListState.scroll 中：
- **正值** = 向下滚动 = **内容向上移动** ✅ 本项目使用
- **负值** = 向上滚动 = 内容向下移动

```kotlin
// 正确：内容向上移动（从下往上滚动）
listState.scroll { scrollBy(scrollPerFrame) }  // scrollPerFrame > 0

// 错误：内容向下移动
listState.scroll { scrollBy(-scrollPerFrame) }
```

### 底部检测

使用 `viewportEndOffset` 而不是 `viewportSize.height`，更准确地检测最后一项是否完全可见：

```kotlin
val viewportEnd = layoutInfo.viewportEndOffset
val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
val isAtBottom = isLastItem && itemBottom <= viewportEnd && totalScrolled > 50f
```
