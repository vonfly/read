# 翻页效果与自动翻页功能 Spec

## 要解决的问题

阅读器的翻页效果和自动翻页功能：

### 翻页模式
- **横滑（SLIDE）**：左右滑动翻页，页面跟随手指移动 ✅
- **覆盖（COVER）**：新页面从右侧覆盖旧页面 ✅
- **仿真（REAL）**：仿真翻页效果（本期暂不实现）

### 自动翻页（滚动模式）✅

**交互流程**：
1. 更多面板开启"自动翻页"开关
2. 开启后**隐藏所有面板**，进入自动滚动模式
3. 内容**从下往上自动滚动**（类似字幕滚动）
4. 点击屏幕任意位置 → **停止滚动** + 显示配置面板
5. 再次点击屏幕（面板外区域）→ **隐藏面板** + **恢复滚动**
6. 点击"退出自动翻页"按钮 → **关闭自动翻页模式**

**效果**：
- 内容连续向上滚动，不是整页翻
- 滚动到当前页底部时自动加载下一页内容
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

| 属性 | 值 |
|------|-----|
| 实现方式 | LazyColumn + LaunchedEffect + scroll { scrollBy() } |
| 滚动方向 | 从下往上（内容向上移动） |
| 基础滚动速度 | 60px/s（速度1x） |
| 点击行为 | 停止滚动，触发 onPause 回调 |
| 布局检测 | 等待 50ms 确保布局完成 |
| 底部检测 | 累计滚动超过 50px 后才判断 |

**滚动速度计算**：
```
滚动速度 = 60px/s × 速度倍数
1x = 60px/s
1.5x = 90px/s
2x = 120px/s
3x = 180px/s
```

**跨页滚动**：
- 当滚动到当前页面底部时，自动切换到下一页
- 内容无缝衔接，滚动位置重置到顶部继续滚动
- 到达最后一页底部时停止滚动并提示

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
- [x] 内容从下往上连续滚动（不是整页翻）
- [x] 滚动速度可配置（1x, 1.5x, 2x, 3x）
- [x] 点击屏幕停止滚动，显示配置面板
- [x] 再次点击屏幕（面板外）隐藏面板，恢复滚动
- [x] 点击"退出自动翻页"按钮，退出自动翻页模式
- [x] 滚动到当前页底部时自动切换到下一页继续滚动
- [x] 到达最后一页底部时停止并提示"已到达最后一页"
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
| 滚动到最后一页底部 | 停止滚动，显示提示 |

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
| AutoScrollReader | AutoScrollReader.kt | 自动滚动阅读组件 ✅ |
| AutoPageConfigPanel | AutoPageConfigPanel.kt | 自动翻页配置面板 ✅ |

---

## UI 结构树

### 阅读内容区域（根据模式）

```
ReaderContent
├── if (autoPageEnabled)
│   └── Box
│       ├── AutoScrollReader (LazyColumn 自动滚动)
│       │   └── 内容区域
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
| 滚动方式 | 从下往上 | `LazyListState.scroll { scrollBy(-amount) }` | ✅ |
| 滚动速度 | 60-180px/s | `60f * speed / 60f` 每帧 | ✅ |
| 点击暂停 | 停止+显示面板 | `pointerInput` + `onPause` 回调 | ✅ |
| 跨页处理 | 自动加载下一页 | 监听 `layoutInfo` 到底部 | ✅ |
| 布局检测 | 等待布局完成 | `isLayoutComplete` + 50ms 延迟 | ✅ |
| 底部检测 | 避免误判 | `totalScrolled > 50f` 阈值 | ✅ |

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

### AutoScrollReader 滚动逻辑

```kotlin
@Composable
fun AutoScrollReader(
    pages: ImmutableList<PageContent>,
    currentPageIndex: Int,
    chapterTitle: String,
    speed: Float,
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
                val layoutInfo = listState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                val isAtBottom = if (lastVisibleItem != null) {
                    val isLastItem = lastVisibleItem.index == layoutInfo.totalItemsCount - 1
                    val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
                    val viewportHeight = layoutInfo.viewportSize.height
                    isLastItem && itemBottom <= viewportHeight && totalScrolled > 50f
                } else false

                if (isAtBottom) {
                    if (renderedPageIndex < pages.size - 1) {
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
        LazyColumn(state = listState, ...) { ... }
    }
}
```

### 配置面板 + 透明遮罩层

```kotlin
// 在 ReaderContent 中
if (uiState.readerSettings.autoPageEnabled) {
    Box(modifier = Modifier.fillMaxSize()) {
        AutoScrollReader(
            isPaused = uiState.autoPagePaused,
            onPause = { setAutoPagePaused(true) },
            ...
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
