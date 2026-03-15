# 目录面板 Spec

## 要解决的问题

阅读器底部控制栏有两种状态，通过点击"目录"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（目录模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为目录内容（章节列表 + 目录/亮度/字体/更多）
- **不是新弹出一个面板，是同一个底部面板替换内容**

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Read-Contents` 页面 → `BottomBar` 组件

### 面板容器（CatalogBottomPanel）

| 属性 | 值 |
|------|-----|
| 宽度 | 375dp（fillMaxWidth） |
| 高度 | 730dp（约占屏幕 90%） |
| 圆角 | 顶部 16dp，底部 0dp |
| 背景 | `currentColorScheme.panelBackground`（随主题变化） |
| 阴影 | blur=20, color=`#00000025`, offset=(0, 4) |
| 布局 | 垂直 |

### 主题颜色适配

> ⚠️ 所有面板内颜色都随主题变化，使用 `currentColorScheme: ReaderColorScheme`

| 元素 | 亮色主题 | 深色（Night）主题 |
|------|----------|-------------------|
| 面板背景 | `currentColorScheme.panelBackground` | `currentColorScheme.panelBackground` |
| 标题颜色 | `currentColorScheme.text` | `currentColorScheme.text` (#E5E5EA) |
| 次要文字 | `currentColorScheme.text.copy(alpha = 0.6f)` | `currentColorScheme.text.copy(alpha = 0.6f)` |
| 边框颜色 | `#F0F0F0` | `#3A3A3A` |
| 章节标题（非当前）| `currentColorScheme.text.copy(alpha = 0.6f)` | `currentColorScheme.text.copy(alpha = 0.6f)` |
| 锁图标 | `currentColorScheme.text.copy(alpha = 0.4f)` | `currentColorScheme.text.copy(alpha = 0.4f)` |

### Header（目录标题栏）

> ⚠️ 所有颜色都随主题变化

| 属性 | 值 |
|------|-----|
| 高度 | 56dp |
| 水平内边距 | 20dp |
| 底部边框 | 1dp, 亮色主题 `#F0F0F0` / 深色主题 `#3A3A3A` |
| 布局 | 水平，两端对齐，垂直居中 |

**标题文字**
| 属性 | 值 |
|------|-----|
| 内容 | "目录" |
| 字体 | Inter |
| 字号 | 18sp |
| 字重 | 600 (SemiBold) |
| 颜色 | `currentColorScheme.text`（随主题变化） |

**排序按钮**
| 属性 | 值 |
|------|-----|
| 布局 | 水平，间距 4dp，垂直居中 |
| 图标 | `arrow-up-down` (Lucide), 16dp, `currentColorScheme.text.copy(alpha = 0.6f)` |
| 文字 | "正序" / "倒序", 14sp, normal, `currentColorScheme.text.copy(alpha = 0.6f)` |

### 目录列表（CatalogList）

| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 上下内边距 | 8dp |
| 宽度 | fillMaxWidth |
| 高度 | fillMaxHeight（占据剩余空间） |

### 章节项（ChapterItem）

| 属性 | 值 |
|------|-----|
| 高度 | 48dp |
| 水平内边距 | 20dp |
| 布局 | 水平，两端对齐，垂直居中 |
| 宽度 | fillMaxWidth |

**章节项类型（主题适配）**

> ⚠️ 所有颜色都随主题变化，使用 `currentColorScheme: ReaderColorScheme`

| 类型 | 背景色 | 标题颜色 | 标题字重 | 右侧标记 |
|------|--------|---------|---------|---------|
| 当前章节 | `currentColorScheme.background.copy(alpha = 0.5f)` | `currentColorScheme.text` | 500 (Medium) | "当前" 12sp, 500, `currentColorScheme.text` |
| 非当前章节 | 透明 | `currentColorScheme.text.copy(alpha = 0.6f)` | normal | "免费" 12sp, 500, `Success` 或 锁图标 14dp, `currentColorScheme.text.copy(alpha = 0.4f)` |

**章节标题文字**
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 15sp |
| 最大行数 | 1 |
| 溢出 | ellipsis |

### 底部控制按钮（复用 BottomControlButtons）

> ⚠️ 复用底部控制按钮组件（目录、亮度、字体、更多），通过 `activeButton` 参数控制高亮。
> 注意：只复用菜单按钮部分，不包含进度条和翻页按钮。

**组件**: `BottomControlButtons`

**目录按钮高亮状态**：

| 属性 | 默认值 | 高亮值 |
|------|--------|--------|
| 图标颜色 | `$--foreground` | `$--accent` → `Accent` (#FF6B4A) |
| 文字颜色 | `$--foreground-tertiary` | `$--accent` → `Accent` (#FF6B4A) |
| 文字字重 | normal | 600 (SemiBold) |

**其他按钮保持默认状态**。

---

## 验收标准

### UI 验收（视觉还原）
- [ ] 面板圆角：顶部 16dp，底部 0dp
- [ ] 面板背景色：随主题变化 (`currentColorScheme.panelBackground`)
- [ ] 面板阴影：blur=20, color=`#00000025`
- [ ] Header 高度 56dp，底部边框颜色随主题变化（亮色 `#F0F0F0` / 深色 `#3A3A3A`）
- [ ] 标题"目录"：18sp, SemiBold, 颜色随主题变化
- [ ] 排序按钮：图标 16dp + 文字 14sp, 颜色使用 60% 透明度
- [ ] 章节项高度 48dp，内边距 20dp
- [ ] 当前章节背景：50% 透明度的主题背景色，标题使用主题文字色 Medium
- [ ] 非当前章节标题：60% 透明度的主题文字色
- [ ] 免费章节标签：`Success` 色（绿色，不随主题变化）
- [ ] 锁定章节图标：14dp, 40% 透明度的主题文字色
- [ ] 底部控制按钮复用 `BottomControlButtons`，目录按钮高亮 `Accent`
- [ ] **深色模式验证**：所有文字在深色背景上清晰可见

### 功能验收
- [ ] 默认状态：TopBar 显示，底部显示进度条 + 控制按钮
- [ ] 点击目录按钮：TopBar 隐藏（fadeOut），底部**原地**切换为目录内容
- [ ] 再次点击目录按钮：TopBar 显示（fadeIn），底部切换回默认控制栏
- [ ] 点击章节项跳转到对应章节并隐藏所有面板
- [ ] 点击排序按钮切换正序/倒序，列表立即刷新
- [ ] 目录按钮高亮（Accent 色）
- [ ] 底部控制栏按钮功能正常（亮度/字体/更多）
- [ ] 面板弹出时点击文字区域：隐藏所有面板
- [ ] 所有面板隐藏时点击中间区域：显示默认控制栏（状态 A）
- [ ] 所有面板隐藏时点击左侧区域：上一页
- [ ] 所有面板隐藏时点击右侧区域：下一页

### 动画验收
- [ ] TopBar 有 fadeIn/fadeOut 动画
- [ ] 底部面板内容切换**无动画**，瞬间完成

### 数据验收
- [ ] 章节列表从 ViewModel 获取
- [ ] 当前章节高亮显示
- [ ] 章节状态（免费/锁定）正确显示

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 底部控制栏"目录"按钮 | 切换到底部面板目录模式，TopBar 隐藏 |
| 目录模式下点击"目录"按钮 | 切换回默认控制栏，TopBar 显示 |
| 点击章节项 | 跳转到对应章节并隐藏所有面板 |
| 点击排序按钮 | 切换正序/倒序，列表立即刷新 |
| 底部控制栏其他按钮 | 正常触发对应功能（待实现） |
| 面板弹出时点击文字区域 | 隐藏所有面板（TopBar + 底部面板） |
| 所有面板隐藏时点击中间区域 | 显示默认控制栏（状态 A：TopBar + 底部面板） |
| 所有面板隐藏时点击左侧区域 | 上一页 |
| 所有面板隐藏时点击右侧区域 | 下一页 |

### 面板切换交互（重要）

**新设计**：底部面板内容**原地切换**，无弹出动画。

**实现方式**：
1. 使用 `when` 表达式切换底部面板内容（无动画）
2. TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）
3. 状态变量：`visiblePanel: ReaderPanel?`
   - `null` = 默认控制栏（显示 TopBar）
   - `CATALOG` = 目录模式（隐藏 TopBar）

---

## 不在本期范围内
- 章节分组（卷/篇）
- 章节搜索
- 章节缩略图预览
- 付费解锁章节功能

---

## 技术约束

### 数据来源
| 数据 | 来源 | 说明 |
|------|------|------|
| 章节列表 | `ReaderUiState.chapters` | 从 ViewModel 获取 |
| 当前章节 | `ReaderUiState.currentChapterIndex` | 高亮显示 |
| 章节状态 | `Chapter.isFree` / `Chapter.isLocked` | 免费或锁定 |

### 复用组件
- `BottomControlButtons`：底部控制按钮（目录/亮度/字体/更多），已从 `ReaderBottomBar` 提取为可复用组件

### 自定义实现（不使用官方组件）
- **不使用** `ModalBottomSheet`：样式不够自定义
- **不使用** `AnimatedContent`：避免弹出感
- **自定义** `CatalogBottomPanel` Composable：原地切换内容

---

## UI 结构树

### 状态 A（默认控制栏）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── ReaderTopBar (返回、书名、加入书架、书签、设置)
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── null → ReaderBottomBar (132dp)
        ├── ProgressBar (50dp, 进度条 + 翻页按钮)
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多)
```

### 状态 B（目录模式）

```
ReaderContent
├── TopBar (AnimatedVisibility, visible = visiblePanel == null)
│   └── (隐藏)
├── ReaderPageLayer (阅读内容)
├── ReaderFooter (时间 + 页码)
└── BottomPanel (when visiblePanel)
    └── CATALOG → CatalogBottomPanel (730dp)
        ├── Header (56dp, 标题 + 排序按钮)
        ├── CatalogList (fillMaxHeight, 章节列表)
        │   ├── ChapterItem (当前章节, PrimaryLight)
        │   ├── ChapterItem (免费章节)
        │   └── ChapterItem (锁定章节)
        └── BottomControlButtons (66dp, 目录/亮度/字体/更多, 目录高亮)
```

---

## 实现对照表

### CatalogBottomPanel（目录底部面板）

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 宽度 | 375dp | `fillMaxWidth()` | ⬜ |
| 高度 | 730dp | `height(730.dp)` | ⬜ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ⬜ |
| 底部圆角 | 0dp | `RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)` | ⬜ |
| 背景色 | #F9F9F9 | `Color(0xFFF9F9F9)` | ⬜ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ⬜ |

### Header（标题栏）

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 56dp | `height(56.dp)` | ⬜ |
| 水平内边距 | 20dp | `padding(horizontal = 20.dp)` | ⬜ |
| 底部边框 | 1dp, #F0F0F0 | `horizontalDivider(1.dp, Color(0xFFF0F0F0))` | ⬜ |
| 标题字号 | 18sp | `fontSize = 18.sp` | ⬜ |
| 标题字重 | 600 | `fontWeight = FontWeight.SemiBold` | ⬜ |
| 标题颜色 | $--foreground | `Foreground` | ⬜ |
| 排序图标 | 16dp | `Modifier.size(16.dp)` | ⬜ |
| 排序图标颜色 | $--foreground-secondary | `ForegroundSecondary` | ⬜ |
| 排序文字 | 14sp | `fontSize = 14.sp` | ⬜ |
| 排序文字颜色 | $--foreground-secondary | `ForegroundSecondary` | ⬜ |

### ChapterItem（章节项）

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 48dp | `height(48.dp)` | ⬜ |
| 水平内边距 | 20dp | `padding(horizontal = 20.dp)` | ⬜ |
| 标题字号 | 15sp | `fontSize = 15.sp` | ⬜ |
| 当前章节背景 | $--primary-light | `PrimaryLight` | ⬜ |
| 当前章节标题 | $--primary, 500 | `Primary`, `FontWeight.Medium` | ⬜ |
| 当前标签字号 | 12sp | `fontSize = 12.sp` | ⬜ |
| 当前标签字重 | 500 | `FontWeight.Medium` | ⬜ |
| 免费标签颜色 | $--success | `Success` | ⬜ |
| 锁图标尺寸 | 14dp | `Modifier.size(14.dp)` | ⬜ |
| 锁图标颜色 | $--foreground-tertiary | `ForegroundTertiary` | ⬜ |

### CatalogList（目录列表）

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 上下内边距 | 8dp | `padding(vertical = 8.dp)` | ⬜ |
| 宽度 | fillMaxWidth | `fillMaxWidth()` | ⬜ |
| 高度 | fillMaxHeight | `weight(1f)` | ⬜ |

### BottomControlButtons 高亮状态

> 复用底部控制按钮（目录、亮度、字体、更多），不包含进度条和翻页按钮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | Foreground | Accent (#FF6B4A) | ⬜ |
| 文字颜色 | ForegroundTertiary | Accent (#FF6B4A) | ⬜ |
| 文字字重 | normal | SemiBold | ⬜ |
