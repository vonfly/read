# 更多面板 Spec

## 要解决的问题

阅读器底部控制栏的更多设置功能，通过点击"更多"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（更多模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为更多设置内容（翻页方式 + 自动翻页 + 控制按钮）
- **不是新弹出一个面板，是同一个底部面板替换内容**

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Read-More` 页面 → `BottomBar` 组件

### 面板容器（MoreBottomPanel）

| 属性 | 值 |
|------|-----|
| 宽度 | 375dp（fillMaxWidth） |
| 高度 | 238dp |
| 圆角 | 顶部 16dp，底部 0dp |
| 背景 | `currentColorScheme.panelBackground`（随主题变化） |
| 顶部边框 | 1dp, 亮色 `#E8E8E8` / 深色 `#3A3A3A` |
| 阴影 | blur=20, color=`#00000025`, offset=(0, 4) |
| 布局 | 垂直 |
| 顶部内边距 | 16dp |

### 主题颜色适配

> ⚠️ 所有面板内颜色都随主题变化，使用 `currentColorScheme: ReaderColorScheme`

| 元素 | 亮色主题 | 深色（Night）主题 |
|------|----------|-------------------|
| 面板背景 | `currentColorScheme.panelBackground` | `currentColorScheme.panelBackground` |
| 标题颜色 | `currentColorScheme.text` | `currentColorScheme.text` (#E5E5EA) |
| 分段控制器背景 | `#F5F5F7` | `#3A3A3A` |
| 选中项背景 | `#FFFFFF` | `#5A5A5A` |
| 选中项文字 | `currentColorScheme.text` | `#E5E5EA` |
| 未选中项背景 | `#F5F5F5` | `#3A3A3A` |
| 未选中项文字 | `currentColorScheme.text.copy(alpha = 0.6f)` | `#E5E5EA` 60% |
| 开关轨道色 | `#E5E5EA` | `#3A3A3A` |
| 开关滑块色 | `#FFFFFF` | `#5A5A5A` |
| 顶部边框 | `#E8E8E8` | `#3A3A3A` |

### PageTurnSection（翻页方式区）

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
| 内容 | "翻页方式" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**翻页选项容器（SegmentedControl）**
| 属性 | 值 |
|------|-----|
| 布局 | 水平 |
| 高度 | 40dp |
| 间距 | 12dp |
| 水平对齐 | space_between |

**翻页选项（3个）**

| 选项 | 值 | 说明 |
|------|-----|------|
| 仿真 | `page_turn_real` | 仿真翻页效果 |
| 覆盖 | `page_turn_cover` | 覆盖翻页效果 |
| 滑动 | `page_turn_swipe` | 滑动翻页效果 |

**选项通用属性**
| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth (weight=1f) |
| 高度 | 36dp |
| 圆角 | 8dp |
| 布局 | 水平居中 + 垂直居中 |

**选中状态**
| 属性 | 值 |
|------|-----|
| 背景 | 亮色: `#FFFFFF` / 深色: `#5A5A5A` |
| 阴影 | blur=2, color=#00000020, offset=(0, 1) |
| 文字颜色 | 亮色: `currentColorScheme.text` / 深色: `#E5E5EA` |
| 文字字重 | 500 (Medium) |

**未选中状态**
| 属性 | 值 |
|------|-----|
| 背景 | 亮色: `#F5F5F5` / 深色: `#3A3A3A` |
| 文字颜色 | `currentColorScheme.text.copy(alpha = 0.6f)` |
| 文字字重 | normal |

### AutoPageSection（自动翻页区）

| 属性 | 值 |
|------|-----|
| 宽度 | fillMaxWidth |
| 高度 | 66dp |
| 布局 | 水平，两端对齐，垂直居中 |
| 内边距 | vertical: 12dp, horizontal: 20dp |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "自动翻页" |
| 字体 | Inter |
| 字号 | 14sp |
| 字重 | 500 (Medium) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**开关组件（Toggle）**
| 属性 | 值 |
|------|-----|
| 宽度 | 51dp |
| 高度 | 31dp |
| 圆角 | 16dp |
| 轨道颜色（关闭） | 亮色: `#E5E5EA` / 深色: `#3A3A3A` |
| 轨道颜色（开启） | `Accent` (#FF6B4A) |
| 滑块尺寸 | 27×27dp |
| 滑块圆角 | 14dp |
| 滑块颜色 | `#FFFFFF` |
| 滑块阴影 | blur=2, color=#00000020, offset=(0, 1) |
| 内边距 | 2dp |

### 底部控制按钮（复用 BottomControlButtons）

> ⚠️ 复用底部控制按钮组件（目录、亮度、字体、更多），通过 `activeButton` 参数控制高亮。

**组件**: `BottomControlButtons`

**更多按钮高亮状态**：

| 属性 | 默认值 | 高亮值 |
|------|--------|--------|
| 图标颜色 | `currentColorScheme.text` | `Accent` (#FF6B4A) |
| 文字颜色 | `currentColorScheme.text.copy(alpha = 0.6f)` | `Accent` (#FF6B4A) |
| 文字字重 | normal | 600 (SemiBold) |

**其他按钮保持默认状态**。

---

## SegmentedControl 背景色（随主题变化）

> 参考 font-spec.md 中的 SegmentedControl 设计规格

| 主题 | SegmentedControl 背景 | 选中项背景 | 选中项文字色 |
|------|------------------------|----------|----------|
| Default | #F5F5F7 | #FFFFFF | currentColorScheme.text |
| Sepia | #E8E0D1 | #FFFFFF | currentColorScheme.text |
| Pink | #E8D4DA | #FFFFFF | currentColorScheme.text |
| Green | #D4E8D5 | #FFFFFF | currentColorScheme.text |
| Night | #3A3A3A | #5A5A5A | #E5E5EA |
| Cream | #F0E8DC | #FFFFFF | currentColorScheme.text |

---

## 验收标准

### UI 验收（视觉还原）
- [ ] 面板高度 238dp
- [ ] 面板圆角：顶部 16dp，底部 0dp
- [ ] 面板背景色：随主题变化（`currentColorScheme.panelBackground`）
- [ ] 面板阴影：blur=20, color=`#00000025`
- [ ] 顶部边框：1dp, 亮色 `#E8E8E8` / 深色 `#3A3A3A`
- [ ] 翻页方式标题：14sp, Medium, 颜色随主题变化
- [ ] 翻页选项：高度 36dp, 圆角 8dp
- [ ] 选中项：白色/灰色背景 + 阴影
- [ ] 未选中项：灰色背景，无阴影
- [ ] 自动翻页标题：14sp, Medium, 颜色随主题变化
- [ ] 开关尺寸：51×31dp，圆角 16dp
- [ ] 开关滑块：27×27dp，白色，带阴影
- [ ] 底部控制按钮复用 `BottomControlButtons`，更多按钮高亮 `Accent`
- [ ] **深色模式验证**：所有元素在深色背景上颜色正确

### 功能验收
- [ ] 默认状态：TopBar 显示，底部显示进度条 + 控制按钮
- [ ] 点击更多按钮：TopBar 隐藏（fadeOut），底部**原地**切换为更多面板
- [ ] 再次点击更多按钮：TopBar 显示（fadeIn），底部切换回默认控制栏
- [ ] 点击翻页方式选项切换翻页模式
- [ ] 切换自动翻页开关
- [ ] 更多按钮高亮（Accent 色）
- [ ] 当前选中的翻页方式显示选中样式
- [ ] 底部控制栏按钮功能正常（目录/亮度/字体）
- [ ] 面板弹出时点击文字区域：隐藏所有面板
- [ ] 所有面板隐藏时点击中间区域：显示默认控制栏（状态 A）

### 动画验收
- [ ] TopBar 有 fadeIn/fadeOut 动画
- [ ] 底部面板内容切换**无动画**，瞬间完成

### 数据验收
- [ ] 翻页方式从 ViewModel 获取并持久化
- [ ] 自动翻页开关状态从 ViewModel 获取并持久化
- [ ] 切换翻页方式后阅读器翻页效果同步变化
- [ ] 开启自动翻页后阅读器开始自动翻页

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 底部控制栏"更多"按钮 | 切换到底部面板更多模式，TopBar 隐藏 |
| 更多模式下点击"更多"按钮 | 切换回默认控制栏，TopBar 显示 |
| 点击翻页方式选项 | 切换翻页模式并保存偏好 |
| 切换自动翻页开关 | 开启/关闭自动翻页功能 |
| 底部控制栏其他按钮 | 正常触发对应功能 |
| 面板弹出时点击文字区域 | 隐藏所有面板（TopBar + 底部面板） |
| 所有面板隐藏时点击中间区域 | 显示默认控制栏（状态 A） |

### 面板切换交互（与目录/亮度/字体面板一致）

**实现方式**：
1. 使用 `when` 表达式切换底部面板内容（无动画）
2. TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）
3. 状态变量：`visiblePanel: ReaderPanel?`
   - `null` = 默认控制栏（显示 TopBar）
   - `MORE` = 更多模式（隐藏 TopBar）

---

## 不在本期范围内
- 翻页速度设置
- 自动翻页间隔时间设置
- 音效开关
- 屏幕方向锁定
- 全屏模式

---

## 技术约束

### 数据来源
| 数据 | 来源 | 说明 |
|------|------|------|
| 翻页方式 | `ReaderPreferencesRepository` | DataStore 持久化 |
| 自动翻页开关 | `ReaderPreferencesRepository` | DataStore 持久化 |

### 复用组件
- `BottomControlButtons`：底部控制按钮（目录/亮度/字体/更多），已从 `ReaderBottomBar` 提取为可复用组件
- `SegmentedControl`：翻页方式分段控制器，复用 `FontBottomPanel` 中的 SegmentedControl 组件

### 主题颜色组件
- `ReaderColorScheme`：阅读器颜色主题枚举，包含 `background`、`text`、`panelBackground` 等属性
- 所有面板通过 `currentColorScheme: ReaderColorScheme` 参数获取主题颜色

### 自定义实现（不使用官方组件）
- **复用** `SegmentedControl`：翻页方式选择器与字体面板的分段控制器样式一致
- **不使用** `ModalBottomSheet`：样式不够自定义
- **自定义** `MoreBottomPanel` Composable：原地切换内容

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

### 状态 B（更多模式）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── (隐藏)
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── MORE → MoreBottomPanel (238dp) - 背景色随主题
        ├── PageTurnSection (90dp, 翻页方式)
        │   ├── Title (14sp, "翻页方式") - 颜色随主题
        │   └── SegmentedControl (40dp, 3个选项)
        │       ├── Option1 (36dp, "仿真") - 选中
        │       ├── Option2 (36dp, "覆盖") - 未选中
        │       └── Option3 (36dp, "滑动") - 未选中
        ├── AutoPageSection (66dp, 自动翻页)
        │   ├── Title (14sp, "自动翻页") - 颜色随主题
        │   └── Toggle (51×31dp, 开关)
        │       └── Thumb (27×27dp, 滑块) - 白色 + 阴影
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多, 更多高亮)
```

---

## 实现对照表

### MoreBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 238dp | `height(238.dp)` | ⬜ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ⬜ |
| 背景色 | 随主题 | `currentColorScheme.panelBackground` | ⬜ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ⬜ |
| 顶部边框 | 1dp, #E8E8E8 | `horizontalDivider(1.dp, ...)` | ⬜ |
| 顶部内边距 | 16dp | `padding(top = 16.dp)` | ⬜ |

### PageTurnSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 90dp | `height(90.dp)` | ⬜ |
| 内边距 | vertical: 12dp, horizontal: 20dp | `padding(vertical = 12.dp, horizontal = 20.dp)` | ⬜ |
| 间距 | 12dp | `verticalArrangement = Arrangement.spacedBy(12.dp)` | ⬜ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ⬜ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ⬜ |
| 标题颜色 | 随主题 | `currentColorScheme.text` | ⬜ |
| 选项高度 | 36dp | `height(36.dp)` | ⬜ |
| 选项圆角 | 8dp | `RoundedCornerShape(8.dp)` | ⬜ |
| 选中背景 | 亮色: #FFFFFF / 深色: #5A5A5A | - | ⬜ |
| 未选中背景 | 亮色: #F5F5F5 / 深色: #3A3A3A | - | ⬜ |

### AutoPageSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 66dp | `height(66.dp)` | ⬜ |
| 内边距 | vertical: 12dp, horizontal: 20dp | `padding(vertical = 12.dp, horizontal = 20.dp)` | ⬜ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ⬜ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ⬜ |
| 标题颜色 | 随主题 | `currentColorScheme.text` | ⬜ |
| 开关宽度 | 51dp | `width(51.dp)` | ⬜ |
| 开关高度 | 31dp | `height(31.dp)` | ⬜ |
| 开关圆角 | 16dp | `RoundedCornerShape(16.dp)` | ⬜ |
| 滑块尺寸 | 27×27dp | `size(27.dp)` | ⬜ |
| 滑块圆角 | 14dp | `RoundedCornerShape(14.dp)` | ⬜ |
| 滑块颜色 | #FFFFFF | `Color.White` | ⬜ |

### BottomControlButtons 高亮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | currentColorScheme.text | Accent | ⬜ |
| 文字颜色 | currentColorScheme.text 60% | Accent | ⬜ |
| 文字字重 | normal | SemiBold | ⬜ |
