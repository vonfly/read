# 字体面板功能实现计划

## 状态概览
- 创建时间：2026-03-15
- 进度：6/6 (100%) ✅

---

## Context

阅读器字体设置功能，通过点击底部控制栏"字体"按钮切换到字体面板，支持调节字体大小、行间距、字间距。

### 现状（已完成）
- ✅ `ReaderPanel.FONT` 枚举值已存在
- ✅ `ReaderSettings` 已有 `fontSize`、`lineHeight`、`letterSpacing` 字段
- ✅ `ReaderPreferencesRepository` 已有 `updateFontSize()`、`updateLineHeight()`、`updateLetterSpacing()` 方法
- ✅ `ReaderViewModel.onFontClick()` 已改为切换面板
- ✅ `FontBottomPanel` 组件已实现
- ✅ `ReaderContent` 中已添加 `ReaderPanel.FONT` 分支

### 目标
实现完整的字体设置面板，支持：
1. 字体大小滑块调节（复用 CustomProgressBar）
2. 行间距分段控制器（4 选项）
3. 字间距分段控制器（4 选项）
4. 设置持久化到 DataStore
5. 实时应用到阅读内容

---

## 第一批：数据层扩展（可并行）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/ReaderSettings.kt` | 新增 `letterSpacing: Float = 0f` 字段 |
| ✅ | `domain/repository/ReaderPreferencesRepository.kt` | 新增 `updateLetterSpacing(letterSpacing: Float)` 接口方法 |

---

## 第二批：数据层实现（依赖第一批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` | 1. 新增 `LETTER_SPACING` key<br>2. `observeSettings()` 中读取 letterSpacing<br>3. 实现 `updateLetterSpacing()` 方法 |

---

## 第三批：ViewModel 层（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | 1. 修改 `onFontClick()` 切换面板<br>2. 新增 `onFontSizeChange(fontSize: Float)`<br>3. 新增 `onLineHeightChange(lineHeight: Float)`<br>4. 新增 `onLetterSpacingChange(letterSpacing: Float)` |

---

## 第四批：UI 组件层（依赖第三批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/components/FontBottomPanel.kt` | **新建**，字体设置面板组件 |

### FontBottomPanel 组件结构（参考 BrightnessBottomPanel）

```
FontBottomPanel (272dp, 顶部圆角 16dp, 阴影向上)
├── FontSizeSection (90dp)
│   ├── 标题 "字体大小" (14sp, Medium)
│   └── 滑块行 (40dp 高度)
│       ├── 小 "A" 标签 (12sp)
│       ├── 滑块 (weight=1f, 复用 CustomProgressBar 样式)
│       └── 大 "A" 标签 (20sp, SemiBold)
│
├── SpacingSection (100dp, 水平排列)
│   ├── LineSpacingGroup (weight=1f)
│   │   ├── 标题 "行间距" (14sp, Medium)
│   │   └── SegmentedControl (40dp, 4选项: 1.0/1.5/1.8/2.2)
│   └── CharSpacingGroup (weight=1f)
│       ├── 标题 "字间距" (14sp, Medium)
│       └── SegmentedControl (40dp, 4选项: 0/1/2/3)
│
└── BottomControlButtons (66dp, 复用组件, activeButton = FONT)
```

### SegmentedControl 组件规格

| 属性 | 值 |
|------|-----|
| 高度 | 40dp |
| 圆角 | 12dp |
| 背景 | 根据主题动态调整（见下表） |
| 内边距 | 4dp |
| 选项高度 | 32dp |
| 选项圆角 | 10dp |
| 选中背景 | 亮色: #FFFFFF / 深色: #5A5A5A |
| 选中阴影 | elevation=4dp, color=#00000035 (21%透明度) |
| 选中文字色 | 亮色: `currentColorScheme.text` / 深色: `#E5E5EA` |
| 未选中背景 | 透明 |

### SegmentedControl 背景色（随主题变化）

| 主题 | SegmentedControl 背景 | 选中项背景 | 选中项文字色 |
|------|------------------------|----------|----------|
| Default | #F5F5F7 | #FFFFFF | currentColorScheme.text |
| Sepia | #E8E0D1 | #FFFFFF | currentColorScheme.text |
| Pink | #E8D4DA | #FFFFFF | currentColorScheme.text |
| Green | #D4E8D5 | #FFFFFF | currentColorScheme.text |
| Night | #3A3A3A | #5A5A5A | #E5E5EA |
| Cream | #F0E8DC | #FFFFFF | currentColorScheme.text |

---

## 第五批：UI 集成层（依赖第四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderContent.kt` | 在 `when (visiblePanel)` 中添加 `FONT` 分支，调用 `FontBottomPanel` |

---

## 复用组件

| 组件 | 文件 | 说明 |
|------|------|------|
| `BottomControlButtons` | `ReaderBottomBar.kt` | 底部控制按钮（目录/亮度/字体/更多），已提取为可复用组件 |
| `CustomProgressBar` 样式 | `ReaderBottomBar.kt` | 字体大小滑块样式与亮度滑块一致，参考实现 |
| 阴影实现 | `BrightnessBottomPanel.kt` | `drawBehind` + `setShadowLayer(dy = -4f)` |

---

## 数据规格

| 设置项 | 选项值 | 默认值 | 说明 |
|--------|--------|--------|------|
| 字体大小 | 滑块连续调节 | 18sp | 范围 12-32sp |
| 行间距 | 1.0 / 1.5 / 1.8 / 2.2 | 1.8 | 分段控制器 |
| 字间距 | 0 / 1 / 2 / 3 | 0 | 分段控制器（sp 单位倍数） |

---

## Verification ✅ 已通过

1. **编译验证**：`./gradlew assembleDebug` ✅ 通过
2. **UI 验收**（需人工在设备上验证）：
   - 点击"字体"按钮，底部面板切换为字体面板，TopBar 隐藏
   - 面板高度 272dp，顶部圆角 16dp
   - 拖动字体大小滑块，阅读内容文字大小实时变化
   - 点击行间距选项，阅读内容行距变化
   - 点击字间距选项，阅读内容字距变化
   - 字体按钮高亮显示（Accent 色）
3. **数据验证**：
   - 退出阅读器后重新进入，设置保持
   - DataStore 中保存 fontSize、lineHeight、letterSpacing 值
4. **交互验证**：
   - 再次点击"字体"按钮，切换回默认控制栏
   - 点击其他按钮（目录/亮度/更多）正常触发对应功能
