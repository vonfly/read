# 目录面板 - 实现计划

## 状态概览
- 创建时间：2026-03-15
- 更新时间：2026-03-15
- 进度：8/8 (100%) ✅

---

## Context

阅读器底部控制栏有两种状态，通过点击"目录"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（目录模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为目录内容（章节列表 + 目录/亮度/字体/更多）
- **不是新弹出一个面板，是同一个底部面板替换内容**

**关键约束**：
- 复用 `BottomControlButtons` 组件（目录/亮度/字体/更多）
- 底部面板内容**原地切换**，无弹出动画
- TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）

---

## 交互对比

| 特性 | 旧设计（弹出） | 新设计（原地切换） |
|------|---------------|-------------------|
| 动画 | fadeIn/fadeOut | 底部面板无动画，TopBar 有动画 |
| TopBar | 始终显示 | 根据状态显示/隐藏 |
| 面板层级 | 叠加在控制栏上 | 同一容器内切换内容 |
| 容器高度 | 730dp（目录）vs 132dp（控制栏） | 动态高度 |

---

## 第一批：数据层 + 状态定义

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderPanel.kt` | 新增 `ReaderPanel` 枚举 |
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 新增 `chapters`、`currentChapterIndex`、`visiblePanel`、`isSortAscending` 字段 |

---

## 第二批：ViewModel 逻辑

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 新增目录面板相关方法，`onCatalogClick()` 只切换 `visiblePanel` |

---

## 第三批：UI 组件

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/CatalogBottomPanel.kt` | 目录底部面板组件（Header + List + BottomControlButtons） |
| ✅ | `ui/screen/reader/components/ReaderBottomBar.kt` | 提取 `BottomControlButtons` 可复用组件 |

---

## 第四批：集成到 ReaderContent

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 重构底部面板：TopBar 绑定 `visiblePanel`，底部面板用 `when` 原地切换 |

---

## 第五批：数据填充

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 初始化 Mock 章节数据 |

---

## 实现对照表（严格遵循 spec 数值）

### CatalogBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 730dp | `height(730.dp)` | ✅ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ✅ |
| 背景色 | #F9F9F9 | `Color(0xFFF9F9F9)` | ✅ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ✅ |

### Header

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 56dp | `height(56.dp)` | ✅ |
| 水平内边距 | 20dp | `padding(horizontal = 20.dp)` | ✅ |
| 底部边框 | 1dp, #F0F0F0 | `HorizontalDivider(1.dp, Color(0xFFF0F0F0))` | ✅ |
| 标题字号 | 18sp | `fontSize = 18.sp` | ✅ |
| 标题字重 | 600 | `fontWeight = FontWeight.SemiBold` | ✅ |
| 排序图标 | 16dp | `Modifier.size(16.dp)` | ✅ |
| 排序文字 | 14sp | `fontSize = 14.sp` | ✅ |

### ChapterItem

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 48dp | `height(48.dp)` | ✅ |
| 水平内边距 | 20dp | `padding(horizontal = 20.dp)` | ✅ |
| 标题字号 | 15sp | `fontSize = 15.sp` | ✅ |
| 当前章节背景 | PrimaryLight | `PrimaryLight` | ✅ |
| 当前章节标题 | Primary, Medium | `Primary`, `FontWeight.Medium` | ✅ |
| 免费标签 | Success, 12sp, 500 | `Success`, `12.sp`, `Medium` | ✅ |
| 锁图标 | 14dp, ForegroundTertiary | `size(14.dp)`, `ForegroundTertiary` | ✅ |

### BottomControlButtons 高亮

> 复用底部控制按钮（目录、亮度、字体、更多），不包含进度条和翻页按钮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | Foreground | Accent | ✅ |
| 文字颜色 | ForegroundTertiary | Accent | ✅ |
| 文字字重 | normal | SemiBold | ✅ |

---

## 验证步骤

1. **编译验证**：`./gradlew assembleDebug` ✅
2. **UI 验证**：✅
   - 默认状态：TopBar 显示，底部显示进度条 + 控制按钮 ✅
   - 点击目录按钮：TopBar 隐藏（fadeOut），底部**原地**切换为目录内容 ✅
   - 再次点击目录按钮：TopBar 显示（fadeIn），底部切换回默认控制栏 ✅
3. **功能验证**：✅
   - 点击章节项跳转到对应章节并隐藏所有面板 ✅
   - 点击排序按钮切换正序/倒序 ✅
   - 目录按钮高亮（Accent 色） ✅
   - 当前章节背景为 PrimaryLight ✅
   - 免费章节显示"免费"标签，锁定章节显示锁图标 ✅
4. **动画验证**：✅
   - TopBar 有 fadeIn/fadeOut 动画 ✅
   - 底部面板内容切换**无动画**，瞬间完成 ✅
5. **点击区域验证**：✅
   - 面板弹出时点击文字区域 → 隐藏所有面板 ✅
   - 所有面板隐藏时点击中间区域 → 显示默认控制栏（状态 A） ✅
   - 所有面板隐藏时点击左侧区域 → 上一页 ✅
   - 所有面板隐藏时点击右侧区域 → 下一页 ✅
6. **面板空白区域点击验证**：✅
   - 点击顶部面板空白区域 → 不隐藏面板 ✅
   - 点击底部面板空白区域 → 不隐藏面板 ✅
   - 点击面板上的按钮 → 正常触发按钮功能 ✅
7. **阴影统一验证**：✅
   - 默认底部面板和目录面板阴影参数一致 ✅

---

## 相关文件

### 已修改的文件
- ✅ `ui/screen/reader/ReaderViewModel.kt` - 调整 `onCatalogClick()` 逻辑
- ✅ `ui/screen/reader/ReaderContent.kt` - 重构底部面板逻辑，TopBar 绑定 `visiblePanel`
- ✅ `ui/screen/reader/components/ReaderBottomBar.kt` - 提取 `BottomControlButtons` 可复用组件

### 已新增的文件
- ✅ `ui/screen/reader/ReaderPanel.kt` - 面板类型枚举
- ✅ `ui/screen/reader/components/CatalogBottomPanel.kt` - 目录底部面板组件（重命名自 CatalogPanel）

### 参考文件（已存在）
- `domain/model/Chapter.kt` - 章节模型
- `docs/features/catalog-spec.md` - 设计规格
