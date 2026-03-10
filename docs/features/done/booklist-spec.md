# 书架页面 Spec

## 要解决的问题
用户需要一个清晰的书架首页，快速继续阅读最近的书，并管理自己的书籍库。

## 用户故事
- 作为读者，我可以快速继续阅读上次未读完的书，以便无缝衔接阅读体验
- 作为读者，我可以浏览书架上所有书籍，以便选择要读的书
- 作为读者，我可以点击书籍进入阅读器，以便开始阅读

---

## 验收标准

### 1. 页面整体布局

| 属性 | 规范值 |
|------|--------|
| 页面背景色 | `#F9F9F7` (--background) |
| 状态栏 | 沉浸式，内容延伸至状态栏下方 |
| 底部安全区 | TabBar 自动处理安全区域内边距 |

### 2. 顶部导航栏 (Header)

| 属性 | 规范值 |
|------|--------|
| 高度 | `44dp` |
| 水平内边距 | `20dp` |
| 标题文字 | "书架"，`28sp`，Bold(700)，颜色 `#1A1A1A` (--foreground) |
| 右侧图标 | 搜索 + 添加，尺寸 `22×22dp`，颜色 `#1A1A1A` |
| 图标间距 | `16dp` |
| 布局 | 标题靠左，图标组靠右，space-between |

**验收点：**
- [ ] 标题左对齐，右侧图标右对齐
- [ ] 搜索图标点击后跳转搜索页（本期 disabled，仅显示）
- [ ] 添加图标点击后弹出导入选项（本期 disabled，仅显示）

### 3. 内容区域 (Content)

| 属性 | 规范值 |
|------|--------|
| 水平内边距 | `20dp` |
| 区块间距 | `24dp` (--spacing-lg) |
| 布局方向 | 垂直 |

### 4. 继续阅读区块 (ContinueReading)

#### 4.1 区块标题栏

| 属性 | 规范值 |
|------|--------|
| 高度 | `24dp` |
| 标题文字 | "继续阅读"，`18sp`，Semibold(600)，颜色 `#1A1A1A` |
| "更多" 文字 | `14sp`，Regular，颜色 `#6B6B6B` (--foreground-secondary) |
| 更多图标 | chevron-right，`16×16dp`，颜色 `#6B6B6B` |
| 标题与更多间距 | `4dp` |
| 区块内间距 | `12dp` (标题与卡片之间) |

**验收点：**
- [ ] 标题左对齐，"更多" 右对齐
- [ ] 点击 "更多" 跳转到阅读记录页（本期 disabled）

#### 4.2 继续阅读卡片 (ContinueCard)

| 属性 | 规范值 |
|------|--------|
| 高度 | `100dp` |
| 圆角 | `12dp` (--radius-md) |
| 背景色 | `#FFFFFF` (--card) |
| 内边距 | `12dp` |
| 封面与信息区间距 | `12dp` |
| 阴影 | `0 2 8 #00000008` (y=2, blur=8) |

**封面 (Cover)：**
| 属性 | 规范值 |
|------|--------|
| 尺寸 | `56×76dp` |
| 圆角 | `6dp` |
| 背景 | 渐变色（每本书不同，示例：`#4facfe → #00f2fe`） |

**书籍信息 (Info)：**
| 属性 | 规范值 |
|------|--------|
| 布局 | 垂直居中 |
| 书名 | `16sp`，Semibold(600)，颜色 `#1A1A1A` |
| 进度文字 | `13sp`，Regular，颜色 `#6B6B6B`，格式："已读 XX% · 第X章" |
| 文字间距 | `4dp` |

**进度条 (ProgressBar)：**
| 属性 | 规范值 |
|------|--------|
| 高度 | `4dp` |
| 圆角 | `2dp` |
| 轨道颜色 | `#E5E5EA` (--slider-track) |
| 已读颜色 | `#C5C5CA` (--slider-active) |

**验收点：**
- [ ] 卡片点击跳转到阅读器，定位到上次阅读位置
- [ ] 无继续阅读记录时，隐藏整个 ContinueReading 区块

### 5. 书架区块 (Bookshelf)

#### 5.1 区块标题栏

| 属性 | 规范值 |
|------|--------|
| 高度 | `24dp` |
| 标题文字 | "我的书架"，`18sp`，Semibold(600)，颜色 `#1A1A1A` |
| "更多" | 本期不显示（disabled） |
| 区块内间距 | `12dp` (标题与网格之间) |

#### 5.2 书籍网格 (Grid)

| 属性 | 规范值 |
|------|--------|
| 布局 | 横向滚动 (LazyRow) 或网格 (LazyVerticalGrid) |
| 项目间距 | `12dp` |
| 每行数量 | 3 列（根据屏幕宽度自适应） |

#### 5.3 书籍卡片 (BookItem)

| 属性 | 规范值 |
|------|--------|
| 宽度 | `100dp` |
| 封面尺寸 | `100×140dp` (宽高比 1:1.4) |
| 封面圆角 | `8dp` (--radius-sm) |
| 封面阴影 | `0 4 12 #00000015` (y=4, blur=12) |
| 封面与标题间距 | `8dp` (--spacing-sm) |
| 书名 | `13sp`，Medium(500)，颜色 `#1A1A1A` |
| 书名对齐 | 居中，单行，超出省略 |
| 书名宽度 | 与封面同宽 `100dp` |

**验收点：**
- [ ] 点击书籍跳转到阅读器
- [ ] 长按书籍弹出删除确认 Dialog
- [ ] 书名过长时显示省略号
- [ ] 空书架时显示空状态占位图 + "暂无书籍，快去添加吧"

### 6. 底部导航栏 (TabBar)

| 属性 | 规范值 |
|------|--------|
| 高度 | `83dp` (含底部安全区) |
| 背景色 | `#FFFFFF` (--card) |
| 顶部边框 | `1dp`，颜色 `#F0F0F0` (--border-light) |
| 顶部阴影 | `0 -2 10 #00000008` (y=-2, blur=10) |
| 内边距 | `top=8, horizontal=24, bottom=24` |

**Tab 项 (TabItem)：**
| 属性 | 规范值 |
|------|--------|
| 尺寸 | `48×48dp` |
| 图标尺寸 | `24×24dp` |
| 标签字号 | `10sp`，Medium(500) |
| 图标与标签间距 | `4dp` |
| 选中颜色 | `#007AFF` (--primary) |
| 未选中颜色 | `#999999` (--foreground-tertiary) |

**Tab 项目：**
| Tab | 图标 | 选中状态 |
|-----|------|---------|
| 书架 | book-open | 本页，选中态 |
| 书城 | compass | 本期 disabled |
| 我的 | user | 本期 disabled |

**验收点：**
- [ ] 当前页面 Tab 高亮显示
- [ ] 点击 "书城" / "我的" 无响应（本期 disabled）

---

## 边缘情况

| 场景 | 处理方式 |
|------|---------|
| 继续阅读无记录 | 隐藏整个 ContinueReading 区块 |
| 书架空状态 | 显示空状态占位图 + 引导文案 |
| 长按书籍 | 弹出删除确认 Dialog，确认后从书架移除 |
| 网络异常 | 纯本地功能，无网络依赖 |

---

## 颜色变量参考

```kotlin
// ui/theme/Color.kt
val Primary = Color(0xFF007AFF)
val Background = Color(0xFFF9F9F7)
val Card = Color(0xFFFFFFFF)
val Foreground = Color(0xFF1A1A1A)
val ForegroundSecondary = Color(0xFF6B6B6B)
val ForegroundTertiary = Color(0xFF999999)
val BorderLight = Color(0xFFF0F0F0)
val SliderTrack = Color(0xFFE5E5EA)
val SliderActive = Color(0xFFC5C5CA)
```

## 间距变量参考

```kotlin
// ui/theme/Dimensions.kt
val SpacingXs = 4.dp
val SpacingSm = 8.dp
val SpacingMd = 12.dp
val SpacingLg = 16.dp
val SpacingXl = 24.dp

val RadiusSm = 8.dp
val RadiusMd = 12.dp
val RadiusLg = 16.dp
```

---

## 不在本期范围内

- 搜索功能
- 书城页面
- 我的页面
- 导入书籍
- 书籍详情页
