# 亮度面板 - 实现计划

## 状态概览
- 创建时间：2026-03-15
- 进度：7/7 (100%) ✅ 已完成
- 最后更新：2026-03-15（Round 3：主题颜色跟随）

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

## Round 3 修复（2026-03-15）：主题颜色跟随

### 问题
1. TopBar 背景色没有随主题变化
2. 深色模式面板背景透明（使用了 80% 透明度）
3. 进度条颜色（轨道、已读部分）没有随主题变化

### 修复方案

| 文件 | 修改内容 | 状态 |
|------|----------|------|
| `ReaderSettings.kt` | Night 主题的 `panelBackground` 从 `#CC2A2A2A` 改为 `#FF1A1A1A`（不透明） | ✅ |
| `ReaderTopBar.kt` | 添加 `currentColorScheme` 参数，背景色和文字颜色随主题变化 | ✅ |
| `ReaderBottomBar.kt` | `CustomProgressBar` 添加 `currentColorScheme` 参数，使用 `sliderTrack` 和 `sliderActive` | ✅ |
| `ReaderContent.kt` | 传递 `currentColorScheme` 到 TopBar | ✅ |

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
| `ReaderColorScheme` | `domain/model/ReaderSettings.kt` | 颜色主题枚举，已有 6 种颜色，包含 `panelBackground`、`sliderTrack`、`sliderActive` 属性 |

### 需要扩展的部分
1. **ReaderColorScheme**：已添加 Pink（粉色）和 Cream（奶油色）两种背景 ✅
2. **ReaderPreferencesRepository**：已添加亮度值的读写方法 ✅
3. **ReaderSettings**：已添加 `brightness` 字段 ✅
4. **ReaderViewModel**：已添加亮度面板切换方法和亮度/背景颜色状态 ✅
5. **ReaderContent.kt**：已集成亮度面板到 `when` 表达式 ✅

---

## 第一批：领域层（数据模型扩展）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/ReaderSettings.kt` | 扩展 `ReaderColorScheme` 添加 Pink、Cream、`panelBackground`、`sliderTrack`、`sliderActive`、`sliderThumbBorder`；添加 `brightness` 字段到 `ReaderSettings` |
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
| ✅ | `ui/screen/reader/components/BrightnessBottomPanel.kt` | 亮度底部面板组件（BrightnessSection + BackgroundSection + BottomControlButtons），支持拖动手势和主题颜色 |
| ✅ | `ui/screen/reader/components/ReaderBottomBar.kt` | 添加 `currentColorScheme` 参数，`CustomProgressBar` 使用主题颜色 |
| ✅ | `ui/screen/reader/components/CatalogBottomPanel.kt` | 添加 `currentColorScheme` 参数，章节高亮使用主题颜色 |
| ✅ | `ui/screen/reader/components/ReaderTopBar.kt` | 添加 `currentColorScheme` 参数，背景色和文字颜色随主题变化 |

---

## 第五批：集成到 ReaderContent

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 在 `when (uiState.visiblePanel)` 中添加 `BRIGHTNESS` 分支，所有面板传递 `uiState.readerSettings.colorScheme` |

---

## 实现对照表（严格遵循 spec 数值）

### BrightnessBottomPanel 容器

| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 高度 | 272dp | `height(272.dp)` | ✅ |
| 顶部圆角 | 16dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ✅ |
| 背景色 | 随主题 | `currentColorScheme.panelBackground` | ✅ |
| 阴影 | blur=20, #00000025 | `drawBehind` + `setShadowLayer` | ✅ |
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
| 滑块 | 自定义实现（参考 CustomProgressBar） | `BrightnessSlider` + 拖动手势 | ✅ |

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

### 背景颜色定义（ReaderColorScheme）

| 名称 | 颜色值 | Compose 变量 | 状态 |
|------|--------|--------------|------|
| 白色 | #F9F9F9 | `Default` | ✅ |
| 米色 | #F5F0E1 | `Sepia` | ✅ |
| 粉色 | #FFE4E8 | `Pink` | ✅ |
| 绿色 | #E8F5E9 | `Green` | ✅ |
| 深色 | #1A1A1A | `Night` | ✅ |
| 奶油 | #FDF8F0 | `Cream` | ✅ |

### 主题颜色属性（panelBackground / sliderTrack / sliderActive）

| 主题 | panelBackground | sliderTrack | sliderActive | 状态 |
|------|-----------------|-------------|--------------|------|
| Default | #F9F9F9 | #E5E5EA | #C5C5CA | ✅ |
| Sepia | #F5F0E1 | #E5E5EA | #C5B8A8 | ✅ |
| Pink | #FFE4E8 | #E5E5EA | #E5A5A8 | ✅ |
| Green | #E8F5E9 | #E5E5EA | #A5C5A5 | ✅ |
| Night | #1A1A1A | #3A3A3A | #8A8A8A | ✅ |
| Cream | #FDF8F0 | #E5E5EA | #C5B8A8 | ✅ |

### BottomControlButtons 高亮

| 属性 | 默认值 | 高亮值 | 状态 |
|------|--------|--------|------|
| 图标颜色 | Foreground | Accent | ✅ |
| 文字颜色 | ForegroundTertiary | Accent | ✅ |
| 文字字重 | normal | SemiBold | ✅ |

---

## 验证步骤

1. **编译验证**：`./gradlew assembleDebug` ✅
2. **UI 验证**：
   - 拖动亮度滑块，确认可以正常调节亮度 ✅
   - 切换不同背景颜色，确认所有面板背景色同步变化 ✅
   - 在深色模式下，确认面板背景使用不透明色 ✅
   - 在目录面板中，确认当前章节高亮颜色与选中背景匹配 ✅
3. **功能验证**：
   - 默认状态：TopBar 显示，底部显示进度条 + 控制按钮 ✅
   - 点击亮度按钮：TopBar 隐藏（fadeOut），底部**原地**切换为亮度面板 ✅
   - 再次点击亮度按钮：TopBar 显示（fadeIn），底部切换回默认控制栏 ✅
4. **动画验证**：
   - TopBar 有 fadeIn/fadeOut 动画 ✅
   - 底部面板内容切换**无动画**，瞬间完成 ✅
5. **数据验证**：
   - 亮度值持久化到 DataStore ✅
   - 背景颜色持久化到 DataStore ✅
   - 切换背景颜色后阅读内容区背景同步变化 ✅

---

## 相关文件

### 已修改的文件
- `domain/model/ReaderSettings.kt` - 扩展颜色枚举，添加亮度字段和主题颜色属性
- `domain/repository/ReaderPreferencesRepository.kt` - 添加亮度读写接口
- `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` - 实现亮度持久化
- `ui/screen/reader/ReaderViewModel.kt` - 亮度面板切换逻辑
- `ui/screen/reader/ReaderContent.kt` - 集成亮度面板，传递颜色方案
- `ui/screen/reader/components/BrightnessBottomPanel.kt` - 亮度底部面板组件
- `ui/screen/reader/components/ReaderBottomBar.kt` - 进度条使用主题颜色
- `ui/screen/reader/components/CatalogBottomPanel.kt` - 目录面板使用主题颜色
- `ui/screen/reader/components/ReaderTopBar.kt` - TopBar 使用主题颜色

### 新增的文件
- `ui/screen/reader/components/BrightnessBottomPanel.kt` - 亮度底部面板组件

### 参考文件（已存在）
- `ui/screen/reader/components/CatalogBottomPanel.kt` - 目录面板结构参考
- `ui/screen/reader/components/ReaderBottomBar.kt` - `CustomProgressBar` 和 `BottomControlButtons` 组件
- `docs/features/brightness-spec.md` - 设计规格
