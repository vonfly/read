# 字体面板 Spec

## 要解决的问题

阅读器底部控制栏的字体设置功能，通过点击"字体"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（字体模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为字体设置内容（字体大小滑块 + 行间距选择 + 字间距选择 + 控制按钮）
- **不是新弹出一个面板，是同一个底部面板替换内容**

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Read-Font` 页面 → `BottomBar` 组件

### 面板容器（FontBottomPanel）

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

### FontSizeSection（字体大小调节区）

| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth |
| 高度 | 90dp |
| 布局 | 垂直 |
| 间距 | 12dp |
| 内边距 | vertical: 12dp, horizontal: 20dp |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "字体大小" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**字体大小滑块容器**
| 属性 | 值 |
|------|-----|
| 布局 | 水平 |
| 高度 | 40dp |
| 间距 | 8dp |
| 垂直对齐 | center |

**小 "A" 标签（左侧）**
| 属性 | 值 |
|------|-----|
| 内容 | "A" |
| 字体 | Inter |
| 字号 | 12sp |
| 字重 | normal |
| 颜色 | `currentColorScheme.text.copy(alpha = 0.5f)`（随主题变化） |

**字体大小滑块**（复用 `CustomProgressBar` 组件）

> ⚠️ 字体大小滑块与亮度滑块、默认进度条样式完全一致，直接复用 `ReaderBottomBar` 中的 `CustomProgressBar` 组件。

| 属性 | 值 | 说明 |
|------|-----|------|
| 组件 | `CustomProgressBar` | 复用，不新建 |
| 宽度 | fillMaxWidth | weight(1f) |
| 高度 | 28dp | 与默认进度条一致 |
| 轨道颜色 | `currentColorScheme.sliderTrack` | 随主题变化 |
| 已读颜色 | `currentColorScheme.sliderActive` | 随主题变化 |
| 滑块尺寸 | 24×24dp | 与默认进度条一致 |
| 滑块颜色 | #FFFFFF | 与默认进度条一致 |

**大 "A" 标签（右侧）**
| 属性 | 值 |
|------|-----|
| 内容 | "A" |
| 字体 | Inter |
| 字号 | 20sp |
| 字重 | 600 (SemiBold) |
| 颜色 | `currentColorScheme.text.copy(alpha = 0.5f)`（随主题变化） |

### SpacingSection（间距选择区）

| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth |
| 高度 | 100dp |
| 布局 | 水平 |
| 间距 | 20dp（两个 Group 之间） |
| 内边距 | vertical: 12dp, horizontal: 20dp |
| 水平对齐 | space_between |

#### LineSpacingGroup（行间距组）

| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 间距 | 8dp |
| 宽度 | fillMaxWidth (weight=1f) |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "行间距" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**分段控制器（SegmentedControl）**
| 属性 | 值 |
|------|-----|
| 高度 | 40dp |
| 圆角 | 12dp |
| 背景 | `#F5F5F7`（亮色）/ `#3A3A3A`（深色） |
| 内边距 | 4dp |
| 布局 | 水平 |
| 间距 | 0dp（选项紧邻） |

**行间距选项（4个）**

| 选项 | 值 | 说明 |
|------|-----|------|
| opt1 | 1.0 | 较紧凑 |
| opt2 | 1.5 | 紧凑 |
| opt3 | 1.8 | 适中（默认） |
| opt4 | 2.2 | 宽松 |

**选项通用属性**
| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth (weight=1f) |
| 高度 | 32dp |
| 圆角 | 10dp |
| 布局 | 水平居中 + 垂直居中 |

**选中状态**
| 属性 | 值 |
|------|-----|
| 背景 | #FFFFFF（白色卡片） |
| 阴影 | blur=3, color=#00000015, offset=(0, 1) |
| 文字颜色 | `currentColorScheme.text` |
| 文字字重 | 500 (Medium) |

**未选中状态**
| 属性 | 值 |
|------|-----|
| 背景 | 透明 |
| 文字颜色 | `currentColorScheme.text.copy(alpha = 0.6f)` |
| 文字字重 | normal |

#### CharSpacingGroup（字间距组）

| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 间距 | 8dp |
| 宽度 | fillMaxWidth (weight=1f) |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "字间距" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**分段控制器（SegmentedControl）**
> 规格与行间距分段控制器完全一致

**字间距选项（4个）**

| 选项 | 值 | 说明 |
|------|-----|------|
| opt1 | 0 | 标准 |
| opt2 | 1 | 适中（默认） |
| opt3 | 2 | 宽松 |
| opt4 | 3 | 较宽松 |

> 选项通用属性、选中状态、未选中状态规格与行间距选项完全一致

### 底部控制按钮（复用 BottomControlButtons）

> ⚠️ 复用底部控制按钮组件（目录、亮度、字体、更多），通过 `activeButton` 参数控制高亮。

**组件**: `BottomControlButtons`

**字体按钮高亮状态**：

| 属性 | 默认值 | 高亮值 |
|------|--------|--------|
| 图标颜色 | `currentColorScheme.text` | `Accent` (#FF6B4A) |
| 文字颜色 | `currentColorScheme.text.copy(alpha = 0.6f)` | `Accent` (#FF6B4A) |
| 文字字重 | normal | 600 (SemiBold) |

**其他按钮保持默认状态**。

---

## 主题颜色规格（ReaderColorScheme）

> ⚠️ 所有面板（TopBar、BottomBar、CatalogBottomPanel、BrightnessBottomPanel、FontBottomPanel）的颜色都随主题变化

### 面板背景色（panelBackground）

| 主题 | 面板背景色 | 说明 |
|------|------------|------|
| Default (白色) | `#F9F9F9` | 与内容区域相同 |
| Sepia (米色) | `#F5F0E1` | 与内容区域相同 |
| Pink (粉色) | `#FFE4E8` | 与内容区域相同 |
| Green (绿色) | `#E8F5E9` | 与内容区域相同 |
| Night (深色) | `#1A1A1A` | 与内容区域相同，不透明 |
| Cream (奶油) | `#FDF8F0` | 与内容区域相同 |

### 分段控制器背景色

| 主题 | 背景色 | 说明 |
|------|--------|------|
| Default | `#F5F5F7` | 浅灰色 |
| Sepia | `#E8E0D1` | 暖米色略深 |
| Pink | `#E8D4DA` | 粉色略深 |
| Green | `#D4E8D5` | 绿色略深 |
| Night | `#3A3A3A` | 深灰色 |
| Cream | `#F0E8DC` | 奶油色略深 |

> 设计原则：SegmentedControl 背景色应与面板背景色协调，形成统一的视觉效果。

### 选中卡片样式

| 主题 | 选中背景 | 选中文字色 | 说明 |
|------|----------|------------|------|
| 亮色主题 | #FFFFFF（白色） | `currentColorScheme.text` | 白色卡片 + 主题文字色 |
| 深色主题 (Night) | `#5A5A5A`（柔和灰色） | `#E5E5EA`（浅灰色） | 避免纯白刺眼，使用柔和对比 |

**通用属性**：
- 阴影：elevation=4dp, color=#00000035 (21%透明度), shape=RoundedCornerShape(10dp)

---

## 验收标准

### UI 验收（视觉还原）
- [ ] 面板高度 272dp
- [ ] 面板圆角：顶部 16dp，底部 0dp
- [ ] 面板背景色：随主题变化（`currentColorScheme.panelBackground`）
- [ ] 面板阴影：blur=20, color=`#00000025`
- [ ] 顶部边框：1dp, `#E8E8E8`
- [ ] 字体大小标题：14sp, Medium, 随主题变化
- [ ] 小 "A" 标签：12sp, normal, 随主题变化 50% 透明度
- [ ] 大 "A" 标签：20sp, SemiBold, 随主题变化 50% 透明度
- [ ] 字体大小滑块复用 `CustomProgressBar`（与亮度滑块样式一致，颜色随主题变化）
- [ ] 行间距标题：14sp, Medium, 随主题变化
- [ ] 字间距标题：14sp, Medium, 随主题变化
- [ ] 分段控制器：高度 40dp，圆角 12dp，内边距 4dp
- [ ] 选项：高度 32dp，圆角 10dp
- [ ] 选中状态：白色背景 + 阴影
- [ ] 未选中状态：透明背景
- [ ] 底部控制按钮复用 `BottomControlButtons`，字体按钮高亮 `Accent`
- [ ] TopBar 背景色随主题变化
- [ ] 默认面板进度条颜色随主题变化
- [ ] 深色模式面板背景不透明

### 功能验收
- [ ] 默认状态：TopBar 显示，底部显示进度条 + 控制按钮
- [ ] 点击字体按钮：TopBar 隐藏（fadeOut），底部**原地**切换为字体面板
- [ ] 再次点击字体按钮：TopBar 显示（fadeIn），底部切换回默认控制栏
- [ ] 拖动字体大小滑块调节阅读字体大小
- [ ] 点击行间距选项切换行间距
- [ ] 点击字间距选项切换字间距
- [ ] 字体按钮高亮（Accent 色）
- [ ] 当前选中的行间距/字间距显示选中样式（白色背景 + 阴影）
- [ ] 底部控制栏按钮功能正常（目录/亮度/更多）
- [ ] 面板弹出时点击文字区域：隐藏所有面板
- [ ] 所有面板隐藏时点击中间区域：显示默认控制栏（状态 A）

### 动画验收
- [ ] TopBar 有 fadeIn/fadeOut 动画
- [ ] 底部面板内容切换**无动画**，瞬间完成

### 数据验收
- [ ] 字体大小值从 ViewModel 获取并持久化
- [ ] 行间距值从 ViewModel 获取并持久化
- [ ] 字间距值从 ViewModel 获取并持久化
- [ ] 切换字体大小后阅读内容区文字大小同步变化
- [ ] 切换行间距后阅读内容区行距同步变化
- [ ] 切换字间距后阅读内容区字距同步变化

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 底部控制栏"字体"按钮 | 切换到底部面板字体模式，TopBar 隐藏 |
| 字体模式下点击"字体"按钮 | 切换回默认控制栏，TopBar 显示 |
| 拖动字体大小滑块 | 实时调节阅读字体大小 |
| 点击行间距选项 | 切换行间距并保存偏好 |
| 点击字间距选项 | 切换字间距并保存偏好 |
| 底部控制栏其他按钮 | 正常触发对应功能 |
| 面板弹出时点击文字区域 | 隐藏所有面板（TopBar + 底部面板） |
| 所有面板隐藏时点击中间区域 | 显示默认控制栏（状态 A） |

### 面板切换交互（与目录/亮度面板一致）

**实现方式**：
1. 使用 `when` 表达式切换底部面板内容（无动画）
2. TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）
3. 状态变量：`visiblePanel: ReaderPanel?`
   - `null` = 默认控制栏（显示 TopBar）
   - `FONT` = 字体模式（隐藏 TopBar）

---

## 不在本期范围内
- 自定义字体（导入本地字体文件）
- 字体粗细选择
- 字体类型选择（宋体、黑体等）
- 段落间距设置
- 页边距设置

---

## 技术约束

### 数据来源
| 数据 | 来源 | 说明 |
|------|------|------|
| 字体大小 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 行间距 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 字间距 | `ReaderPreferencesRepository` | DataStore 持久化 |

### 复用组件
- `BottomControlButtons`：底部控制按钮（目录/亮度/字体/更多），已从 `ReaderBottomBar` 提取为可复用组件
- `CustomProgressBar`：字体大小滑块直接复用默认面板的进度条组件（样式与亮度滑块完全一致）

### 主题颜色组件
- `ReaderColorScheme`：阅读器颜色主题枚举，包含 `background`、`text`、`panelBackground`、`sliderTrack`、`sliderActive`、`sliderThumbBorder` 属性
- 所有面板通过 `currentColorScheme: ReaderColorScheme` 参数获取主题颜色

### 自定义实现（不使用官方组件）
- **复用** `CustomProgressBar`：字体大小滑块与亮度滑块样式完全一致，直接复用
- **不使用** `ModalBottomSheet`：样式不够自定义
- **新建** `SegmentedControl` Composable：用于行间距/字间距选择
- **自定义** `FontBottomPanel` Composable：原地切换内容

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

### 状态 B（字体模式）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── (隐藏)
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── FONT → FontBottomPanel (272dp) - 背景色随主题
        ├── FontSizeSection (90dp, 字体大小滑块)
        │   ├── Title (14sp, "字体大小") - 颜色随主题
        │   └── SliderRow (40dp)
        │       ├── LabelSmall (12sp, "A") - 颜色随主题 50% 透明度
        │       ├── Slider (fillMaxWidth) - 颜色随主题
        │       └── LabelLarge (20sp, "A") - 颜色随主题 50% 透明度
        ├── SpacingSection (100dp, 间距选择)
        │   ├── LineSpacingGroup (fillMaxWidth, weight=1f)
        │   │   ├── Title (14sp, "行间距") - 颜色随主题
        │   │   └── SegmentedControl (40dp, 4个选项)
        │   │       ├── Option1 (32dp, "1.0") - 透明背景
        │   │       ├── Option2 (32dp, "1.5") - 透明背景
        │   │       ├── Option3 (32dp, "1.8") - 白色背景 + 阴影（选中）
        │   │       └── Option4 (32dp, "2.2") - 透明背景
        │   └── CharSpacingGroup (fillMaxWidth, weight=1f)
        │       ├── Title (14sp, "字间距") - 颜色随主题
        │       └── SegmentedControl (40dp, 4个选项)
        │           ├── Option1 (32dp, "0") - 透明背景
        │           ├── Option2 (32dp, "1") - 白色背景 + 阴影（选中）
        │           ├── Option3 (32dp, "2") - 透明背景
        │           └── Option4 (32dp, "3") - 透明背景
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多, 字体高亮)
```

---

## 实现对照表

### FontBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 272dp | `height(272.dp)` | ⬜ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ⬜ |
| 背景色 | 随主题 | `currentColorScheme.panelBackground` | ⬜ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ⬜ |
| 顶部边框 | 1dp, #E8E8E8 | （可选，参考亮度面板已移除）| ⬜ |
| 顶部内边距 | 16dp | `padding(top = 16.dp)` | ⬜ |

### FontSizeSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 90dp | `height(90.dp)` | ⬜ |
| 内边距 | vertical: 12dp, horizontal: 20dp | `padding(vertical = 12.dp, horizontal = 20.dp)` | ⬜ |
| 间距 | 12dp | `verticalArrangement = Arrangement.spacedBy(12.dp)` | ⬜ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ⬜ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ⬜ |
| 标题颜色 | 随主题 | `currentColorScheme.text` | ⬜ |
| 小 A 字号 | 12sp | `fontSize = 12.sp` | ⬜ |
| 小 A 颜色 | 随主题 50% | `currentColorScheme.text.copy(alpha = 0.5f)` | ⬜ |
| 大 A 字号 | 20sp | `fontSize = 20.sp` | ⬜ |
| 大 A 字重 | 600 | `fontWeight = FontWeight.SemiBold` | ⬜ |
| 大 A 颜色 | 随主题 50% | `currentColorScheme.text.copy(alpha = 0.5f)` | ⬜ |
| 滑块 | 复用 CustomProgressBar | `CustomProgressBar` | ⬜ |

### SpacingSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 100dp | `height(100.dp)` | ⬜ |
| 内边距 | vertical: 12dp, horizontal: 20dp | `padding(vertical = 12.dp, horizontal = 20.dp)` | ⬜ |
| 间距 | 20dp | `horizontalArrangement = Arrangement.spacedBy(20.dp)` | ⬜ |
| 布局 | 水平 space_between | `horizontalArrangement = Arrangement.SpaceBetween` | ⬜ |

### SegmentedControl

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 40dp | `height(40.dp)` | ⬜ |
| 圆角 | 12dp | `RoundedCornerShape(12.dp)` | ⬜ |
| 背景（亮色） | #F5F5F7 | `Color(0xFFF5F5F7)` | ⬜ |
| 背景（深色） | #3A3A3A | `Color(0xFF3A3A3A)` | ⬜ |
| 内边距 | 4dp | `padding(4.dp)` | ⬜ |
| 选项高度 | 32dp | `height(32.dp)` | ⬜ |
| 选项圆角 | 10dp | `RoundedCornerShape(10.dp)` | ⬜ |
| 选中背景 | #FFFFFF | `Color.White` | ⬜ |
| 选中阴影 | blur=3, #00000015 | `shadow(elevation = 3.dp)` | ⬜ |
| 未选中背景 | 透明 | `Color.Transparent` | ⬜ |

### BottomControlButtons 高亮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | currentColorScheme.text | Accent | ⬜ |
| 文字颜色 | currentColorScheme.text 60% | Accent | ⬜ |
| 文字字重 | normal | SemiBold | ⬜ |
