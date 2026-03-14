# 阅读器页面 Spec

## 要解决的问题
用户需要一个沉浸式的阅读界面，支持 EPUB/TXT/PDF 格式的书籍阅读，提供翻页、进度追踪、章节导航、阅读设置等核心功能。

---

## 设计规格
> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Read` 和 `Read-Control-Bar` 页面

### 页面整体（Read / Read-Control-Bar）
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 812 dp |
| 背景 | `$--background` (#F9F9F7) |
| 布局 | none（绝对定位层叠） |

---

### ReadingLayer（阅读内容层）
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 812 dp |
| 布局 | vertical |
| 背景 | `$--background` |

#### Header（章节标题区）
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 72 dp |
| 布局 | vertical |
| 内边距 | top: 28 dp, horizontal: 20 dp |

**ChapterTitle（章节标题）**
| 属性 | 值 |
|------|-----|
| 内容 | "第15章 科学边界(三)" |
| 字体 | Inter |
| 字号 | 14 sp |
| 字重 | 500 (Medium) |
| 颜色 | `$--foreground-secondary` |

#### Content（阅读内容区）
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | fill_container |
| 布局 | vertical |
| 间距 | 20 dp |
| 内边距 | horizontal: 20 dp |

**段落文本（Paragraph）**
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 18 sp |
| 字重 | normal (400) |
| 行高 | 1.8 |
| 颜色 | `#333333` |
| 宽度 | fill_container |

#### Footer（底部状态栏）
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 24 dp |
| 布局 | horizontal |
| justifyContent | space_between |
| alignItems | center |
| 内边距 | horizontal: 24 dp, bottom: 8 dp |

**TimeText（当前时间）**
| 属性 | 值 |
|------|-----|
| 内容 | "14:30" |
| 字体 | Inter |
| 字号 | 12 sp |
| 字重 | normal (400) |
| 颜色 | `$--foreground-tertiary` |

**PageText（页码）**
| 属性 | 值 |
|------|-----|
| 内容 | "1/128" |
| 字体 | Inter |
| 字号 | 12 sp |
| 字重 | normal (400) |
| 颜色 | `$--foreground-tertiary` |

---

### TopBar（顶部控制栏）
> 控制栏默认隐藏，点击屏幕中央显示/隐藏

| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 72 dp |
| 位置 | x: 0, y: 0 |
| 背景 | `#F9F9F9CC` (80% 不透明度) |
| 模糊 | background_blur radius: 20 |
| justifyContent | space_between |
| alignItems | center |
| 内边距 | horizontal: 20 dp |
| enabled | false（默认隐藏） |

**topLeft（返回区域）**
| 属性 | 值 |
|------|-----|
| 布局 | horizontal |
| 间距 | 16 dp |
| alignItems | center |

**backBtn（返回按钮）**
| 属性 | 值 |
|------|-----|
| 图标 | lucide:chevron-left |
| 尺寸 | 24×24 dp |
| 颜色 | `$--foreground` |

**bookTitle（书名）**
| 属性 | 值 |
|------|-----|
| 内容 | "三体" |
| 字体 | Inter |
| 字号 | 17 sp |
| 字重 | 600 (SemiBold) |
| 颜色 | `$--foreground` |

**ReadingTopActions（顶部操作按钮组）**
| 属性 | 值 |
|------|-----|
| 布局 | horizontal |
| 高度 | 24 dp |
| alignItems | center |
| 间距 | 24 dp |

| 按钮 | 图标 | 尺寸 | 颜色 |
|------|------|------|------|
| shelfIcon（加入书架） | lucide:book-plus | 22×22 dp | `$--accent` |
| bookmarkIcon（书签） | lucide:bookmark | 22×22 dp | `$--foreground-secondary` |
| moreIcon（更多设置） | lucide:settings | 22×22 dp | `$--foreground-secondary` |

---

### BottomBar（底部控制栏）
> 控制栏默认隐藏，点击屏幕中央显示/隐藏

| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 122 dp |
| 位置 | x: 0, y: 690 |
| 圆角 | top-left: 16 dp, top-right: 16 dp |
| 背景 | `#F9F9F9FF` |
| 阴影 | blur: 20, color: `#00000025`, offset: (0, 4) |
| 边框 | top: 1 dp, 颜色: `#E8E8E8` |
| 布局 | vertical |
| 内边距 | top: 16 dp |
| enabled | false（默认隐藏） |

#### ProgressBar（进度条区域）
| 属性 | 值 |
|------|-----|
| 宽度 | fill_container |
| 高度 | 40 dp |
| 布局 | horizontal |
| justifyContent | space_between |
| alignItems | center |
| 间距 | 12 dp |
| 内边距 | horizontal: 20 dp, bottom: 16 dp |

**prevBtn（上一章按钮）**
| 属性 | 值 |
|------|-----|
| 尺寸 | 40×40 dp |
| 圆角 | 20 dp |
| 背景 | transparent |
| justifyContent | center |
| alignItems | center |

**prevIcon**
| 属性 | 值 |
|------|-----|
| 图标 | lucide:chevron-left |
| 尺寸 | 24×24 dp |
| 颜色 | `$--foreground` |

**progressTrack（进度滑块组件）**
| 属性 | 值 |
|------|-----|
| 宽度 | fill_container |
| 高度 | 28 dp |

**readPortion（已读进度）**
| 属性 | 值 |
|------|-----|
| 宽度 | 120 dp（示例值，实际根据进度动态变化） |
| 高度 | 28 dp |
| 圆角 | 14 dp |
| 背景 | `$--slider-active` (#C5C5CA) |
| 阴影 | blur: 4, color: `#00000020`, offset: (0, 2) |

**nextBtn（下一章按钮）**
| 属性 | 值 |
|------|-----|
| 尺寸 | 40×40 dp |
| 圆角 | 20 dp |
| 背景 | transparent |
| justifyContent | center |
| alignItems | center |

**nextIcon**
| 属性 | 值 |
|------|-----|
| 图标 | lucide:chevron-right |
| 尺寸 | 24×24 dp |
| 颜色 | `$--foreground` |

#### BottomControls（底部操作按钮组）
| 属性 | 值 |
|------|-----|
| 宽度 | fill_container |
| 高度 | 66 dp |
| 布局 | horizontal |
| alignItems | center |
| 内边距 | horizontal: 20 dp |

| 按钮 | 图标 | 图标尺寸 | 标签 | 标签字号 | 标签颜色 |
|------|------|----------|------|----------|----------|
| catalogBtn（目录） | lucide:list | 22×22 dp | "目录" | 10 sp | `$--foreground-tertiary` |
| brightnessBtn（亮度） | lucide:sun | 22×22 dp | "亮度" | 10 sp | `$--foreground-tertiary` |
| fontBtn（字体） | lucide:type | 22×22 dp | "字体" | 10 sp | `$--foreground-tertiary` |
| moreBtn（更多） | lucide:layout-grid | 22×22 dp | "更多" | 10 sp | `$--foreground-tertiary` |

**按钮通用属性：**
| 属性 | 值 |
|------|-----|
| 宽度 | fill_container（均分） |
| 高度 | 66 dp |
| 布局 | vertical |
| justifyContent | center |
| alignItems | center |
| 间距 | 4 dp |

**选中状态：**
- 图标颜色变为 `$--accent` (#FF6B4A)
- 标签字重变为 600 (SemiBold)
- 标签颜色变为 `$--accent`

---

## UI 结构树

```
Read (375×812, fill=$--background, layout=none)
├── ReadingLayer (375×812, fill=$--background, layout=vertical)
│   ├── Header (375×72, layout=vertical, padding=[28,0,0,20])
│   │   └── ChapterTitle (14sp, weight=500, color=$--foreground-secondary)
│   ├── Content (375×fill_container, layout=vertical, gap=20, padding=[0,20])
│   │   └── Paragraph (18sp, lineHeight=1.8, color=#333333)
│   └── Footer (375×24, layout=horizontal, padding=[0,24,8,24])
│       ├── TimeText (12sp, color=$--foreground-tertiary)
│       └── PageText (12sp, color=$--foreground-tertiary)
├── TopBar (375×72, y=0, fill=#F9F9F9CC, blur=20, enabled=false)
│   ├── topLeft (layout=horizontal, gap=16)
│   │   ├── backBtn (24×24, lucide:chevron-left)
│   │   └── bookTitle (17sp, weight=600)
│   └── ReadingTopActions (layout=horizontal, gap=24)
│       ├── shelfIcon (22×22, lucide:book-plus, color=$--accent)
│       ├── bookmarkIcon (22×22, lucide:bookmark)
│       └── moreIcon (22×22, lucide:settings)
└── BottomBar (375×122, y=690, cornerRadius=[16,16,0,0], enabled=false)
    ├── ProgressBar (fill_container×40, layout=horizontal, gap=12)
    │   ├── prevBtn (40×40, cornerRadius=20)
    │   │   └── prevIcon (24×24, lucide:chevron-left)
    │   ├── progressTrack (fill_container)
    │   │   └── readPortion (120×28, cornerRadius=14, fill=$--slider-active)
    │   └── nextBtn (40×40, cornerRadius=20)
    │       └── nextIcon (24×24, lucide:chevron-right)
    └── BottomControls (fill_container×66, layout=horizontal, padding=[0,20])
        ├── catalogBtn (fill_container×66, layout=vertical, gap=4)
        ├── brightnessBtn (fill_container×66, layout=vertical, gap=4)
        ├── fontBtn (fill_container×66, layout=vertical, gap=4)
        └── moreBtn (fill_container×66, layout=vertical, gap=4)
```

---

## 验收标准

### UI 验收（视觉还原）
- [ ] 页面背景色使用 `$--background` 变量
- [ ] 章节标题字号 14 sp、字重 500、颜色 `$--foreground-secondary`
- [ ] 段落文本字号 18 sp、行高 1.8、颜色 `#333333`
- [ ] 段落间距 20 dp、水平内边距 20 dp
- [ ] 底部状态栏高度 24 dp、内边距正确
- [ ] 时间和页码字号 12 sp、颜色 `$--foreground-tertiary`
- [ ] TopBar 背景模糊效果正确（blur: 20）
- [ ] TopBar 返回按钮 24×24 dp、书名字号 17 sp
- [ ] TopBar 操作按钮间距 24 dp、图标尺寸 22×22 dp
- [ ] 加入书架图标颜色 `$--accent`，其他按钮 `$--foreground-secondary`
- [ ] BottomBar 圆角正确（顶部 16 dp）
- [ ] BottomBar 阴影效果正确（blur: 20, color: #00000025）
- [ ] BottomBar 顶部边框 1 dp、颜色 `#E8E8E8`
- [ ] 翻页按钮尺寸 40×40 dp、圆角 20 dp
- [ ] 进度条高度 28 dp、圆角 14 dp
- [ ] 底部按钮高度 66 dp、均分宽度
- [ ] 底部按钮图标 22×22 dp、标签字号 10 sp
- [ ] 选中按钮图标和标签颜色变为 `$--accent`

### 功能验收
- [ ] 点击屏幕中央显示/隐藏控制栏
- [ ] 点击返回按钮返回上一页
- [ ] 点击上一章/下一章按钮切换章节
- [ ] 滑动进度条跳转到对应位置
- [ ] 点击目录按钮显示章节目录（本期 Toast）
- [ ] 点击亮度按钮调节屏幕亮度（本期 Toast）
- [ ] 点击字体按钮调整字体设置（本期 Toast）
- [ ] 点击更多按钮显示更多设置（本期 Toast）
- [ ] 点击加入书架按钮添加到书架（本期 Toast）
- [ ] 点击书签按钮添加/移除书签（本期 Toast）
- [ ] 点击设置按钮打开阅读设置（本期 Toast）
- [ ] 阅读进度自动保存（Room 持久化）
- [ ] 进入阅读器时恢复上次阅读位置

### 数据验收
- [ ] 书籍内容从本地文件解析（EPUB/TXT）
- [ ] 章节标题正确显示
- [ ] 当前时间实时更新
- [ ] 页码/进度正确计算和显示
- [ ] 阅读进度持久化到 Room

---

## 交互说明
| 入口 | 行为 |
|------|------|
| 点击屏幕中央 | 显示/隐藏控制栏（TopBar + BottomBar） |
| 点击返回按钮 | 返回书架页面 |
| 点击上一章按钮 | 跳转到上一章开头 |
| 点击下一章按钮 | 跳转到下一章开头 |
| 滑动进度条 | 跳转到对应阅读位置 |
| 点击目录按钮 | 本期 Toast "目录功能开发中" |
| 点击亮度按钮 | 本期 Toast "亮度调节功能开发中" |
| 点击字体按钮 | 本期 Toast "字体设置功能开发中" |
| 点击更多按钮 | 本期 Toast "更多设置功能开发中" |
| 点击加入书架按钮 | 本期 Toast "加入书架功能开发中" |
| 点击书签按钮 | 本期 Toast "书签功能开发中" |
| 点击设置按钮 | 本期 Toast "阅读设置功能开发中" |
| 左右滑动 | 翻页（下一页/上一页） |

---

## 不在本期范围内
- 章节目录侧边栏（点击目录按钮仅 Toast）
- 亮度调节面板（点击亮度按钮仅 Toast）
- 字体设置面板（点击字体按钮仅 Toast）
- 更多设置面板（点击更多按钮仅 Toast）
- 书签添加/管理功能（点击书签按钮仅 Toast）
- 加入书架功能（点击加入书架按钮仅 Toast）
- 阅读设置面板（点击设置按钮仅 Toast）
- 深色/护眼主题切换
- PDF 格式支持（本期仅 EPUB/TXT）
- 翻页动画效果
- 长按选词/划线/笔记
- 听书（TTS）功能
- 云端同步

---

## 技术约束

| 数据 | 来源 | 说明 |
|------|------|------|
| 书籍内容 | 本地文件解析 | EPUB/TXT 文件，分段缓存到内存 |
| 阅读进度 | Room | 当前章节、页码、滚动位置 |
| 章节信息 | 本地文件解析 | 从 EPUB/TXT 提取章节结构 |
| 当前时间 | 系统时间 | 实时更新 |

### 复用组件
- 无（阅读器为独立功能模块）

### 性能要求
- 长文本分段渲染，避免一次性加载全部内容
- 使用 LazyColumn 渲染段落，支持虚拟化
- 翻页响应时间 < 100ms
- 进度保存防抖 500ms，避免频繁 IO

---

## 实现对照表（Agent 必须严格遵循）

> ⚠️ 以下数值必须精确实现，禁止使用"习惯值"或主题变量默认值

### ReadingLayer - Header
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| Header 高度 | 72 dp | `.height(72.dp)` | ⬜ |
| Header 内边距 | top: 28 dp, horizontal: 20 dp | `.padding(top = 28.dp, start = 20.dp, end = 20.dp)` | ⬜ |
| ChapterTitle 字号 | 14 sp | `fontSize = 14.sp` | ⬜ |
| ChapterTitle 字重 | 500 (Medium) | `fontWeight = FontWeight.Medium` | ⬜ |
| ChapterTitle 颜色 | $--foreground-secondary | `ForegroundSecondary` | ⬜ |

### ReadingLayer - Content
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 段落间距 | 20 dp | `verticalArrangement = Arrangement.spacedBy(20.dp)` | ⬜ |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` | ⬜ |
| 段落字号 | 18 sp | `fontSize = 18.sp` | ⬜ |
| 段落行高 | 1.8 | `lineHeight = 18.sp * 1.3f`（注：Compose lineHeight 是倍数） | ⬜ |
| 段落颜色 | #333333 | `Color(0xFF333333)` | ⬜ |

### ReadingLayer - Footer
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| Footer 高度 | 24 dp | `.height(24.dp)` | ⬜ |
| 水平内边距 | 24 dp | `.padding(horizontal = 24.dp)` | ⬜ |
| 底部内边距 | 8 dp | `.padding(bottom = 8.dp)` | ⬜ |
| 时间/页码字号 | 12 sp | `fontSize = 12.sp` | ⬜ |
| 时间/页码颜色 | $--foreground-tertiary | `ForegroundTertiary` | ⬜ |

### TopBar
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| TopBar 高度 | 72 dp | `.height(72.dp)` | ⬜ |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` | ⬜ |
| 背景 | #F9F9F9CC (80% 不透明度) | `Color(0xFFF9F9F9).copy(alpha = 0.8f)` | ⬜ |
| 返回按钮尺寸 | 24×24 dp | `.size(24.dp)` | ⬜ |
| 书名字号 | 17 sp | `fontSize = 17.sp` | ⬜ |
| 书名字重 | 600 (SemiBold) | `fontWeight = FontWeight.SemiBold` | ⬜ |
| 书名颜色 | $--foreground | `Foreground` | ⬜ |
| 操作按钮间距 | 24 dp | `horizontalArrangement = Arrangement.spacedBy(24.dp)` | ⬜ |
| 操作按钮图标尺寸 | 22×22 dp | `.size(22.dp)` | ⬜ |
| 加入书架图标颜色 | $--accent | `Accent` | ⬜ |
| 其他按钮图标颜色 | $--foreground-secondary | `ForegroundSecondary` | ⬜ |

### BottomBar
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| BottomBar 高度 | 122 dp | `.height(122.dp)` | ⬜ |
| 顶部圆角 | 16 dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` | ⬜ |
| 背景 | #F9F9F9FF | `Color(0xFFF9F9F9)` | ⬜ |
| 顶部内边距 | 16 dp | `.padding(top = 16.dp)` | ⬜ |
| 顶部边框 | 1 dp, #E8E8E8 | `Modifier.border(1.dp, Color(0xFFE8E8E8), RectangleShape)` | ⬜ |

### BottomBar - ProgressBar
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| ProgressBar 高度 | 40 dp | `.height(40.dp)` | ⬜ |
| 间距 | 12 dp | `horizontalArrangement = Arrangement.spacedBy(12.dp)` | ⬜ |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` | ⬜ |
| 底部内边距 | 16 dp | `.padding(bottom = 16.dp)` | ⬜ |
| 翻页按钮尺寸 | 40×40 dp | `.size(40.dp)` | ⬜ |
| 翻页按钮圆角 | 20 dp | `RoundedCornerShape(20.dp)` | ⬜ |
| 翻页图标尺寸 | 24×24 dp | `.size(24.dp)` | ⬜ |
| 进度条高度 | 28 dp | `.height(28.dp)` | ⬜ |
| 进度条圆角 | 14 dp | `RoundedCornerShape(14.dp)` | ⬜ |
| 进度条已读颜色 | $--slider-active | `SliderActive` | ⬜ |

### BottomBar - BottomControls
| 属性 | 设计值 | Compose 实现 | 状态 |
|------|--------|--------------|------|
| 按钮组高度 | 66 dp | `.height(66.dp)` | ⬜ |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` | ⬜ |
| 按钮内间距 | 4 dp | `verticalArrangement = Arrangement.spacedBy(4.dp)` | ⬜ |
| 图标尺寸 | 22×22 dp | `.size(22.dp)` | ⬜ |
| 标签字号 | 10 sp | `fontSize = 10.sp` | ⬜ |
| 标签颜色 | $--foreground-tertiary | `ForegroundTertiary` | ⬜ |
| 选中时图标颜色 | $--accent | `Accent` | ⬜ |
| 选中时标签颜色 | $--accent | `Accent` | ⬜ |
| 选中时标签字重 | 600 (SemiBold) | `fontWeight = FontWeight.SemiBold` | ⬜ |

### 颜色变量映射表
| 设计变量 | 颜色值 | Compose 变量 |
|----------|--------|--------------|
| $--accent | #FF6B4A | `Accent` |
| $--background | #F9F9F7 | `Background` |
| $--foreground | #1A1A1A | `Foreground` |
| $--foreground-secondary | #6B6B6B | `ForegroundSecondary` |
| $--foreground-tertiary | #999999 | `ForegroundTertiary` |
| $--slider-active | #C5C5CA | `SliderActive` |
| #333333 | #333333 | `Color(0xFF333333)` |
| #E8E8E8 | #E8E8E8 | `Color(0xFFE8E8E8)` |
| #F9F9F9CC | #F9F9F9 (80% alpha) | `Color(0xFFF9F9F9).copy(alpha = 0.8f)` |
| #00000025 | #000000 (15% alpha) | `Color(0x25000000)` |
