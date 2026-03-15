# 亮度面板 Spec

## 要解决的问题

阅读器底部控制栏的亮度设置功能，通过点击"亮度"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（亮度模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为亮度设置内容（亮度滑块 + 背景选择 + 控制按钮）
- **不是新弹出一个面板，是同一个底部面板替换内容**

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Read-Brightness` 页面 → `BottomBar` 组件

### 面板容器（BrightnessBottomPanel）

| 属性 | 值 |
|------|-----|
| 宽度 | 375dp（fillMaxWidth） |
| 高度 | 272dp |
| 圆角 | 顶部 16dp，底部 0dp |
| 背景 | `currentColorScheme.panelBackground`（随主题变化） |
| 顶部边框 | 1dp, `#E8E8E8` |
| 阴影 | blur=20, color=`#00000025`, offset=(0, 4) |
| 布局 | 垂直 |
| 顶部内边距 | 16dp |

### BrightnessSection（亮度调节区）

| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth |
| 高度 | 90dp |
| 布局 | 垂直 |
| 间距 | 12dp |
| 内边距 | vertical: 8dp, horizontal: 20dp |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "亮度" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**亮度滑块容器**
| 属性 | 值 |
|------|-----|
| 布局 | 水平 |
| 高度 | 28dp |
| 间距 | 12dp |
| 垂直对齐 | center |

**小太阳图标（左侧）**
| 属性 | 值 |
|------|-----|
| 图标 | lucide:sun-dim |
| 尺寸 | 18×18dp |
| 颜色 | `currentColorScheme.text.copy(alpha = 0.5f)`（随主题变化） |

**亮度滑块**（复用 `CustomProgressBar` 组件）

> ⚠️ 亮度滑块与默认面板的进度条样式完全一致，直接复用 `ReaderBottomBar` 中的 `CustomProgressBar` 组件。
> Pencil 设计稿中 `brightnessSlider` 引用了 `ProgressBar` 组件（ref: "uZ5JU"）。

| 属性 | 值 | 说明 |
|------|-----|------|
| 组件 | `CustomProgressBar` | 复用，不新建 |
| 宽度 | fillMaxWidth | weight(1f) |
| 高度 | 28dp | 与默认进度条一致 |
| 轨道颜色 | `currentColorScheme.sliderTrack` | 随主题变化 |
| 已读颜色 | `currentColorScheme.sliderActive` | 随主题变化 |
| 滑块尺寸 | 24×24dp | 与默认进度条一致 |
| 滑块颜色 | #FFFFFF | 与默认进度条一致 |

**大太阳图标（右侧）**
| 属性 | 值 |
|------|-----|
| 图标 | lucide:sun |
| 尺寸 | 22×22dp |
| 颜色 | `currentColorScheme.text`（随主题变化） |

### BackgroundSection（背景选择区）

| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth |
| 高度 | 100dp |
| 布局 | 垂直 |
| 间距 | 12dp |
| 内边距 | vertical: 8dp, horizontal: 20dp |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "背景" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `$--foreground` → `Foreground` |

**颜色选项容器**
| 属性 | 值 |
|------|-----|
| 布局 | 水平 |
| 高度 | 40dp |
| 间距 | 16dp |
| 水平对齐 | space_between |

**颜色选项（ColorOption）**

| 选项 | 背景色 | 名称 | 说明 |
|------|--------|------|------|
| 白色 | `#F9F9F9` | colorWhite | 默认背景 |
| 米色 | `#F5F0E1` | colorBeige | 护眼米色 |
| 粉色 | `#FFE4E8` | colorPink | 粉色护眼 |
| 绿色 | `#E8F5E9` | colorGreen | 绿色护眼 |
| 深色 | `#1A1A1A` | colorDark | 夜间模式 |
| 自定义 | `#FDF8F0` | colorCustom | 奶油色（预留自定义） |

**颜色选项通用属性**
| 属性 | 值 |
|------|-----|
| 尺寸 | 40×40dp |
| 圆角 | 20dp |
| 布局 | 水平居中 + 垂直居中 |

**选中状态**
| 属性 | 值 |
|------|-----|
| 边框 | 2dp, `$--accent` → `Accent` (#FF6B4A) |
| 图标 | lucide:check, 16×16dp, `$--accent` |

**未选中状态**
| 属性 | 值 |
|------|-----|
| 边框 | 1dp, `#E0E0E0` |

### 底部控制按钮（复用 BottomControlButtons）

> ⚠️ 复用底部控制按钮组件（目录、亮度、字体、更多），通过 `activeButton` 参数控制高亮。

**组件**: `BottomControlButtons`

**亮度按钮高亮状态**：

| 属性 | 默认值 | 高亮值 |
|------|--------|--------|
| 图标颜色 | `$--foreground` | `$--accent` → `Accent` (#FF6B4A) |
| 文字颜色 | `$--foreground-tertiary` | `$--accent` → `Accent` (#FF6B4A) |
| 文字字重 | normal | 600 (SemiBold) |

**其他按钮保持默认状态**。

---

## 主题颜色规格（ReaderColorScheme）

> ⚠️ 所有面板（TopBar、BottomBar、CatalogBottomPanel、BrightnessBottomPanel）的颜色都随主题变化

### 面板背景色（panelBackground）

| 主题 | 面板背景色 | 说明 |
|------|------------|------|
| Default (白色) | `#F9F9F9` | 与内容区域相同 |
| Sepia (米色) | `#F5F0E1` | 与内容区域相同 |
| Pink (粉色) | `#FFE4E8` | 与内容区域相同 |
| Green (绿色) | `#E8F5E9` | 与内容区域相同 |
| Night (深色) | `#1A1A1A` | 与内容区域相同，不透明 |
| Cream (奶油) | `#FDF8F0` | 与内容区域相同 |

### 滑块颜色（sliderTrack / sliderActive / sliderThumbBorder）

| 主题 | 轨道 (sliderTrack) | 已读 (sliderActive) | 滑块边框 |
|------|-------------------|-------------------|---------|
| Default (白色) | `#E5E5EA` | `#C5C5CA` | `#25000000` |
| Sepia (米色) | `#E5E5EA` | `#C5B8A8` | `#25000000` |
| Pink (粉色) | `#E5E5EA` | `#E5A5A8` | `#25000000` |
| Green (绿色) | `#E5E5EA` | `#A5C5A5` | `#25000000` |
| Night (深色) | `#3A3A3A` | `#8A8A8A` | `#33FFFFFF` |
| Cream (奶油) | `#E5E5EA` | `#C5B8A8` | `#25000000` |

---

## 验收标准

### UI 验收（视觉还原）
- [x] 面板高度 272dp
- [x] 面板圆角：顶部 16dp，底部 0dp
- [x] 面板背景色：随主题变化（`currentColorScheme.panelBackground`）
- [x] 面板阴影：blur=20, color=`#00000025`
- [x] 顶部边框：1dp, `#E8E8E8`
- [x] 亮度标题：14sp, Medium, 随主题变化
- [x] 小太阳图标：18dp, 随主题变化
- [x] 大太阳图标：22dp, 随主题变化
- [x] 亮度滑块复用 `CustomProgressBar`（与默认进度条样式一致，颜色随主题变化）
- [x] 背景标题：14sp, Medium, `Foreground`
- [x] 颜色选项尺寸 40×40dp，圆角 20dp
- [x] 选中状态边框 2dp `Accent`，勾选图标 16dp
- [x] 未选中状态边框 1dp `#E0E0E0`
- [x] 底部控制按钮复用 `BottomControlButtons`，亮度按钮高亮 `Accent`
- [x] TopBar 背景色随主题变化
- [x] 默认面板进度条颜色随主题变化
- [x] 深色模式面板背景不透明

### 功能验收
- [x] 默认状态：TopBar 显示，底部显示进度条 + 控制按钮
- [x] 点击亮度按钮：TopBar 隐藏（fadeOut），底部**原地**切换为亮度面板
- [x] 再次点击亮度按钮：TopBar 显示（fadeIn），底部切换回默认控制栏
- [x] 拖动亮度滑块调节屏幕亮度
- [x] 点击背景颜色选项切换阅读背景
- [x] 亮度按钮高亮（Accent 色）
- [x] 当前选中的背景颜色显示勾选图标
- [x] 底部控制栏按钮功能正常（目录/字体/更多）
- [x] 面板弹出时点击文字区域：隐藏所有面板
- [x] 所有面板隐藏时点击中间区域：显示默认控制栏（状态 A）

### 动画验收
- [x] TopBar 有 fadeIn/fadeOut 动画
- [x] 底部面板内容切换**无动画**，瞬间完成

### 数据验收
- [x] 亮度值从 ViewModel 获取并持久化
- [x] 背景颜色从 ViewModel 获取并持久化
- [x] 切换背景颜色后阅读内容区背景同步变化

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 底部控制栏"亮度"按钮 | 切换到底部面板亮度模式，TopBar 隐藏 |
| 亮度模式下点击"亮度"按钮 | 切换回默认控制栏，TopBar 显示 |
| 拖动亮度滑块 | 实时调节屏幕亮度 |
| 点击背景颜色选项 | 切换阅读背景色并保存偏好 |
| 底部控制栏其他按钮 | 正常触发对应功能 |
| 面板弹出时点击文字区域 | 隐藏所有面板（TopBar + 底部面板） |
| 所有面板隐藏时点击中间区域 | 显示默认控制栏（状态 A） |

### 面板切换交互（与目录面板一致）

**实现方式**：
1. 使用 `when` 表达式切换底部面板内容（无动画）
2. TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）
3. 状态变量：`visiblePanel: ReaderPanel?`
   - `null` = 默认控制栏（显示 TopBar）
   - `BRIGHTNESS` = 亮度模式（隐藏 TopBar）

---

## 不在本期范围内
- 自定义背景颜色（颜色选择器）
- 背景颜色跟随系统深色模式
- 亮度自动调节（跟随系统）
- 护眼模式定时切换

---

## 技术约束

### 数据来源
| 数据 | 来源 | 说明 |
|------|------|------|
| 亮度值 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 背景颜色 | `ReaderPreferencesRepository` | DataStore 持久化 |

### 复用组件
- `BottomControlButtons`：底部控制按钮（目录/亮度/字体/更多），已从 `ReaderBottomBar` 提取为可复用组件
- `CustomProgressBar`：亮度滑块直接复用默认面板的进度条组件（Pencil 设计稿确认：`brightnessSlider` 引用 `ProgressBar` 组件，样式完全一致）

### 主题颜色组件
- `ReaderColorScheme`：阅读器颜色主题枚举，包含 `background`、`text`、`panelBackground`、`sliderTrack`、`sliderActive`、`sliderThumbBorder` 属性
- 所有面板通过 `currentColorScheme: ReaderColorScheme` 参数获取主题颜色

### 自定义实现（不使用官方组件）
- **复用** `CustomProgressBar`：亮度滑块与默认进度条样式完全一致，直接复用
- **不使用** `ModalBottomSheet`：样式不够自定义
- **自定义** `BrightnessBottomPanel` Composable：原地切换内容

---

## UI 结构树

### 状态 A（默认控制栏）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── ReaderTopBar (返回、书名、加入书架、书签、设置) - 背景色随主题
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── null → ReaderBottomBar (132dp) - 背景色随主题
        ├── ProgressBar (50dp, 进度条 + 翻页按钮) - 颜色随主题
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多)
```

### 状态 B（亮度模式）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── (隐藏)
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── BRIGHTNESS → BrightnessBottomPanel (272dp) - 背景色随主题
        ├── BrightnessSection (90dp, 亮度滑块)
        │   ├── Title (14sp, "亮度") - 颜色随主题
        │   └── SliderRow (28dp)
        │       ├── SunSmall (18dp, sun-dim) - 颜色随主题
        │       ├── Slider (fillMaxWidth) - 颜色随主题
        │       └── SunLarge (22dp, sun) - 颜色随主题
        ├── BackgroundSection (100dp, 背景选择)
        │   ├── Title (14sp, "背景")
        │   └── ColorOptions (40dp, 6个颜色圆圈)
        │       ├── ColorOption (白色, 选中)
        │       ├── ColorOption (米色)
        │       ├── ColorOption (粉色)
        │       ├── ColorOption (绿色)
        │       ├── ColorOption (深色)
        │       └── ColorOption (自定义)
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多, 亮度高亮)
```

---

## 实现对照表

### BrightnessBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 272dp | `height(272.dp)` | ✅ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ✅ |
| 背景色 | 随主题 | `currentColorScheme.panelBackground` | ✅ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ✅ |
| 顶部边框 | 1dp, #E8E8E8 | （已移除）| - |
| 顶部内边距 | 16dp | `padding(top = 16.dp)` | ✅ |

### BrightnessSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 90dp | `height(90.dp)` | ✅ |
| 内边距 | vertical: 8dp, horizontal: 20dp | `padding(vertical = 8.dp, horizontal = 20.dp)` | ✅ |
| 间距 | 12dp | `verticalArrangement = Arrangement.spacedBy(12.dp)` | ✅ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ✅ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ✅ |
| 标题颜色 | 随主题 | `currentColorScheme.text` | ✅ |
| 小太阳尺寸 | 18dp | `size(18.dp)` | ✅ |
| 小太阳颜色 | 随主题 | `currentColorScheme.text.copy(alpha = 0.5f)` | ✅ |
| 大太阳尺寸 | 22dp | `size(22.dp)` | ✅ |
| 大太阳颜色 | 随主题 | `currentColorScheme.text` | ✅ |
| 滑块 | 复用 CustomProgressBar | `BrightnessSlider` + 拖动手势 | ✅ |

### BackgroundSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 100dp | `height(100.dp)` | ✅ |
| 内边距 | vertical: 8dp, horizontal: 20dp | `padding(vertical = 8.dp, horizontal = 20.dp)` | ✅ |
| 间距 | 12dp | `verticalArrangement = Arrangement.spacedBy(12.dp)` | ✅ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ✅ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ✅ |
| 标题颜色 | $--foreground | `Foreground` | ✅ |
| 颜色选项尺寸 | 40×40dp | `size(40.dp)` | ✅ |
| 颜色选项圆角 | 20dp | `CircleShape` | ✅ |
| 选中边框 | 2dp, $--accent | `border(2.dp, Accent, CircleShape)` | ✅ |
| 未选中边框 | 1dp, #E0E0E0 | `border(1.dp, Color(0xFFE0E0E0), CircleShape)` | ✅ |
| 勾选图标尺寸 | 16dp | `size(16.dp)` | ✅ |
| 勾选图标颜色 | $--accent | `Accent` | ✅ |

### 主题颜色（ReaderColorScheme）

| 主题 | 背景色 | 文字色 | 面板背景 | 轨道色 | 已读色 | 状态 |
|------|--------|--------|----------|--------|--------|------|
| Default | #F9F9F9 | #333333 | #F9F9F9 | #E5E5EA | #C5C5CA | ✅ |
| Sepia | #F5F0E1 | #5B4636 | #F5F0E1 | #E5E5EA | #C5B8A8 | ✅ |
| Pink | #FFE4E8 | #5B4636 | #FFE4E8 | #E5E5EA | #E5A5A8 | ✅ |
| Green | #E8F5E9 | #1A3A1A | #E8F5E9 | #E5E5EA | #A5C5A5 | ✅ |
| Night | #1A1A1A | #E5E5EA | #1A1A1A | #3A3A3A | #8A8A8A | ✅ |
| Cream | #FDF8F0 | #5B4636 | #FDF8F0 | #E5E5EA | #C5B8A8 | ✅ |

### BottomControlButtons 高亮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | Foreground | Accent | ✅ |
| 文字颜色 | ForegroundTertiary | Accent | ✅ |
| 文字字重 | normal | SemiBold | ✅ |
