# 亮度面板 - 实现计划

## 状态概览
- 创建时间：2026-03-15
- 进度：7/7 (100%) ✅ 已完成

---

## Context

阅读器底部控制栏有两种状态，通过点击"亮度"按钮切换：

**状态 A（默认）**：
- 显示 TopBar
- 底部显示主控制面板（进度条 + 目录/亮度/字体/更多）

**状态 B（亮度模式）**：
- 隐藏 TopBar
- 底部面板**原地切换**为亮度设置内容（亮度滑块 + 背景选择 + 控制按钮）

**关键约束**：
- 复用 `BottomControlButtons` 组件（目录/亮度/字体/更多）
- 复用 `CustomProgressBar` 组件作为亮度滑块（与默认进度条样式完全一致）
- 底部面板内容**原地切换**，无弹出动画
- TopBar 用 `AnimatedVisibility` 控制显示/隐藏（有 fadeIn/fadeOut 动画）
- 交互模式与已完成的目录面板（`CatalogBottomPanel`）保持一致

---

## 现有架构分析

### 已存在的组件
| 组件 | 文件路径 | 说明 |
|------|----------|------|
| `ReaderPanel` | `ui/screen/reader/ReaderPanel.kt` | 面板类型枚举，已包含 `BRIGHTNESS` |
| `BottomControlButtons` | `ui/screen/reader/components/ReaderBottomBar.kt` | 底部控制按钮，已提取为可复用组件 |
| `CustomProgressBar` | `ui/screen/reader/components/ReaderBottomBar.kt` | 进度条组件，可复用作为亮度滑块 |
| `CatalogBottomPanel` | `ui/screen/reader/components/CatalogBottomPanel.kt` | 目录面板，结构可参考 |
| `ReaderPreferencesRepository` | `domain/repository/ReaderPreferencesRepository.kt` | 阅读器偏好设置接口 |
| `ReaderColorScheme` | `domain/model/ReaderSettings.kt` | 颜色主题枚举，已有 4 种颜色 |

### 需要扩展的部分
1. **ReaderColorScheme**：添加 Pink（粉色）和 Cream（奶油色）两种背景
2. **ReaderPreferencesRepository**：添加亮度值的读写方法
3. **ReaderSettings**：添加 `brightness` 字段
4. **ReaderViewModel**：添加亮度面板切换方法和亮度/背景颜色状态
5. **ReaderContent.kt**：集成亮度面板到 `when` 表达式

---

## 第一批：领域层（数据模型扩展）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/ReaderSettings.kt` | 扩展 `ReaderColorScheme` 添加 Pink、Cream；添加 `brightness` 字段到 `ReaderSettings` |
| ✅ | `domain/repository/ReaderPreferencesRepository.kt` | 添加 `brightness` 的读写方法 |

---

## 第二批：数据层（持久化实现）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` | 实现亮度值的 DataStore 持久化 |

---

## 第三批：ViewModel 逻辑

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 修改 `onBrightnessClick()` 为切换 `visiblePanel`；添加亮度值和背景颜色的状态及更新方法 |

---

## 第四批：UI 组件

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/BrightnessBottomPanel.kt` | 亮度底部面板组件（BrightnessSection + BackgroundSection + BottomControlButtons） |

---

## 第五批：集成到 ReaderContent

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 在 `when (uiState.visiblePanel)` 中添加 `BRIGHTNESS` 分支，调用 `BrightnessBottomPanel` |

---

## 实现对照表（严格遵循 spec 数值）

### BrightnessBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 272dp | `height(272.dp)` | ✅ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ✅ |
| 背景色 | #F9F9F9 | `Color(0xFFF9F9F9)` | ✅ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ✅ |
| 顶部边框 | 1dp, #E8E8E8 | `border(1.dp, Color(0xFFE8E8E8))` | ✅ |
| 顶部内边距 | 16dp | `padding(top = 16.dp)` | ✅ |

### BrightnessSection

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 90dp | `height(90.dp)` | ✅ |
| 内边距 | vertical: 8dp, horizontal: 20dp | `padding(vertical = 8.dp, horizontal = 20.dp)` | ✅ |
| 间距 | 12dp | `verticalArrangement = Arrangement.spacedBy(12.dp)` | ✅ |
| 标题字号 | 14sp | `fontSize = 14.sp` | ✅ |
| 标题字重 | 500 | `fontWeight = FontWeight.Medium` | ✅ |
| 标题颜色 | $--foreground | `Foreground` | ✅ |
| 小太阳尺寸 | 18dp | `size(18.dp)` | ✅ |
| 小太阳颜色 | $--foreground-tertiary | `ForegroundTertiary` | ✅ |
| 大太阳尺寸 | 22dp | `size(22.dp)` | ✅ |
| 大太阳颜色 | $--foreground | `Foreground` | ✅ |
| 滑块 | 自定义实现（参考 CustomProgressBar） | `BrightnessSlider` | ✅ |

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
| 颜色选项圆角 | 20dp | `RoundedCornerShape(20.dp)` | ✅ |
| 选中边框 | 2dp, $--accent | `border(2.dp, Accent, CircleShape)` | ✅ |
| 未选中边框 | 1dp, #E0E0E0 | `border(1.dp, Color(0xFFE0E0E0), CircleShape)` | ✅ |
| 勾选图标尺寸 | 16dp | `size(16.dp)` | ✅ |
| 勾选图标颜色 | $--accent | `Accent` | ✅ |

### 背景颜色定义（扩展 ReaderColorScheme）

| 名称 | 颜色值 | Compose 变量 | 状态 |
|------|--------|--------------|------|
| 白色 | #F9F9F9 | `Default` (已存在，需修改背景色) | ✅ |
| 米色 | #F5F0E1 | `Sepia` (已存在，需修改背景色) | ✅ |
| 粉色 | #FFE4E8 | `Pink` (新增) | ✅ |
| 绿色 | #E8F5E9 | `Green` (已存在，需修改背景色) | ✅ |
| 深色 | #1A1A1A | `Night` (已存在，需修改背景色) | ✅ |
| 奶油 | #FDF8F0 | `Cream` (新增) | ✅ |

### BottomControlButtons 高亮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | Foreground | Accent | ✅ (已实现) |
| 文字颜色 | ForegroundTertiary | Accent | ✅ (已实现) |
| 文字字重 | normal | SemiBold | ✅ (已实现) |

---

## 验证步骤

1. **编译验证**：`./gradlew assembleDebug`
2. **UI 验证**：
   - 默认状态：TopBar 显示，底部显示进度条 + 控制按钮
   - 点击亮度按钮：TopBar 隐藏（fadeOut），底部**原地**切换为亮度面板
   - 再次点击亮度按钮：TopBar 显示（fadeIn），底部切换回默认控制栏
3. **功能验证**：
   - 拖动亮度滑块调节屏幕亮度
   - 点击背景颜色选项切换阅读背景
   - 亮度按钮高亮（Accent 色）
   - 当前选中的背景颜色显示勾选图标
   - 底部控制栏按钮功能正常（目录/字体/更多）
4. **动画验证**：
   - TopBar 有 fadeIn/fadeOut 动画
   - 底部面板内容切换**无动画**，瞬间完成
5. **数据验证**：
   - 亮度值持久化到 DataStore
   - 背景颜色持久化到 DataStore
   - 切换背景颜色后阅读内容区背景同步变化
6. **点击区域验证**：
   - 面板弹出时点击文字区域 → 隐藏所有面板
   - 所有面板隐藏时点击中间区域 → 显示默认控制栏

---

## 相关文件

### 需要修改的文件
- `domain/model/ReaderSettings.kt` - 扩展颜色枚举，添加亮度字段
- `domain/repository/ReaderPreferencesRepository.kt` - 添加亮度读写接口
- `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` - 实现亮度持久化
- `ui/screen/reader/ReaderViewModel.kt` - 亮度面板切换逻辑
- `ui/screen/reader/ReaderContent.kt` - 集成亮度面板

### 需要新增的文件
- `ui/screen/reader/components/BrightnessBottomPanel.kt` - 亮度底部面板组件

### 参考文件（已存在）
- `ui/screen/reader/components/CatalogBottomPanel.kt` - 目录面板结构参考
- `ui/screen/reader/components/ReaderBottomBar.kt` - `CustomProgressBar` 和 `BottomControlButtons` 组件
- `docs/features/brightness-spec.md` - 设计规格
