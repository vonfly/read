# 我的页面（Profile）Spec

## 要解决的问题

用户需要一个个人中心页面，用于查看阅读统计、管理个人数据和访问常用功能入口。

---

## 设计规格

> 以下数据来自 Pencil 设计稿 `pencil-new.pen` → `Profile-Me` 页面

### 页面整体
| 属性 | 值 |
|------|-----|
| 宽度 | 375 dp |
| 高度 | 812 dp |
| 背景 | `$--background` |
| 布局 | 垂直（Column） |

### 顶部标题栏（Header）
| 属性 | 值 |
|------|-----|
| 高度 | 44 dp |
| 水平内边距 | 左右各 20 dp |
| 对齐方式 | spaceBetween（标题左侧，图标右侧） |
| 垂直对齐 | center |

**标题文本**
| 属性 | 值 |
|------|-----|
| 内容 | "我的" |
| 字体 | Inter |
| 字号 | 28 sp |
| 字重 | 700 (Bold) |
| 颜色 | `$--foreground` |

**设置图标**
| 属性 | 值 |
|------|-----|
| 图标 | lucide/settings |
| 尺寸 | 22 × 22 dp |
| 颜色 | `$--foreground` |

---

### 内容区域（Content）
| 属性 | 值 |
|------|-----|
| 布局 | 垂直（Column） |
| 水平内边距 | 20 dp |
| 垂直内边距 | 20 dp |
| 子元素间距 | 20 dp |
| 宽度 | fillContainer |
| 高度 | fillContainer（占满剩余空间） |

---

### 用户信息卡片（UserCard）
| 属性 | 值 |
|------|-----|
| 宽度 | fillContainer |
| 内边距 | 20 dp |
| 圆角 | 16 dp |
| 背景 | `$--card` |
| 阴影 | offset(0, 2), blur=8, color=#00000008 |
| 子元素间距 | 16 dp |
| 对齐方式 | center（垂直居中） |

**头像（Avatar）**
| 属性 | 值 |
|------|-----|
| 尺寸 | 64 × 64 dp |
| 圆角 | 32 dp（圆形） |
| 填充 | 渐变 |
| 渐变类型 | linear |
| 渐变角度 | 135° |
| 渐变颜色 | #667eea (0%) → #764ba2 (100%) |

**用户信息区（profileInfo）**
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 宽度 | fillContainer |
| 子元素间距 | 4 dp |

**用户名（profileName）**
| 属性 | 值 |
|------|-----|
| 内容 | "阅读爱好者" |
| 字体 | Inter |
| 字号 | 18 sp |
| 字重 | 600 (SemiBold) |
| 颜色 | `$--foreground` |

**用户 ID（profileId）**
| 属性 | 值 |
|------|-----|
| 内容 | "ID: 12345678" |
| 字体 | Inter |
| 字号 | 13 sp |
| 字重 | normal |
| 颜色 | `$--foreground-secondary` |

**箭头图标（profileChevron）**
| 属性 | 值 |
|------|-----|
| 图标 | lucide/chevron-right |
| 尺寸 | 20 × 20 dp |
| 颜色 | `$--foreground-tertiary` |

---

### 统计卡片（StatsCard）
| 属性 | 值 |
|------|-----|
| 宽度 | fillContainer |
| 内边距 | 20 dp |
| 圆角 | 16 dp |
| 背景 | `$--card` |
| 阴影 | offset(0, 2), blur=8, color=#00000008 |
| 对齐方式 | spaceBetween（三等分） |

**统计项（StatItem）- 每项结构相同**
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 宽度 | fillContainer（三等分） |
| 对齐 | center（水平居中） |
| 子元素间距 | 4 dp |

**统计数值**
| 属性 | 已读书籍 | 阅读小时 | 收藏书籍 |
|------|---------|---------|---------|
| 示例值 | "128" | "256" | "32" |
| 字号 | 24 sp | 24 sp | 24 sp |
| 字重 | 700 (Bold) | 700 (Bold) | 700 (Bold) |
| 颜色 | `$--primary` | `$--accent` | `$--success` |

**统计标签**
| 属性 | 值 |
|------|-----|
| 内容 | "已读书籍" / "阅读小时" / "收藏书籍" |
| 字体 | Inter |
| 字号 | 12 sp |
| 字重 | normal |
| 颜色 | `$--foreground-secondary` |

---

### 菜单区域（MenuSection）
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 宽度 | fillContainer |
| 水平内边距 | 16 dp |
| 圆角 | 16 dp |
| 背景 | `$--card` |

**菜单项（MenuItem）- 通用结构**
| 属性 | 值 |
|------|-----|
| 高度 | 52 dp |
| 宽度 | fillContainer |
| 对齐方式 | spaceBetween（左侧内容，右侧箭头） |
| 垂直对齐 | center |

**菜单项左侧内容**
| 属性 | 值 |
|------|-----|
| 布局 | 水平 |
| 子元素间距 | 12 dp |
| 对齐 | center（垂直居中） |

**菜单图标**
| 菜单项 | 图标 | 颜色 | 尺寸 |
|--------|------|------|------|
| 我的书签 | lucide/bookmark | `$--primary` | 20 × 20 dp |
| 阅读记录 | lucide/history | `$--accent` | 20 × 20 dp |
| 离线下载 | lucide/download | `$--success` | 20 × 20 dp |
| 夜间模式 | lucide/moon | #8B5CF6 | 20 × 20 dp |

**菜单文本**
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 15 sp |
| 字重 | normal |
| 颜色 | `$--foreground` |

**箭头图标**
| 属性 | 值 |
|------|-----|
| 图标 | lucide/chevron-right |
| 尺寸 | 18 × 18 dp |
| 颜色 | `$--foreground-tertiary` |

**分割线（Divider）**
| 属性 | 值 |
|------|-----|
| 高度 | 1 dp |
| 宽度 | fillContainer |
| 颜色 | `$--border-light` |

---

### 底部导航栏（TabBar）
| 属性 | 值 |
|------|-----|
| 高度 | 83 dp |
| 宽度 | fillContainer |
| 内边距 | top=8, left/right=24, bottom=24 |
| 对齐方式 | spaceBetween（三等分） |
| 垂直对齐 | center |
| 背景 | `$--card` |
| 顶部边框 | 1 dp, `$--border-light` |
| 阴影 | offset(0, -2), blur=10, color=#00000008 |

**TabItem 结构**
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 尺寸 | 48 × 48 dp |
| 对齐 | center（水平居中） |
| 子元素间距 | 4 dp |

**TabItem 图标**
| Tab | 图标 | 尺寸 | 未选中颜色 | 选中颜色 |
|-----|------|------|-----------|---------|
| 书架 | lucide/book-open | 24 × 24 dp | `$--foreground-tertiary` | - |
| 书城 | lucide/compass | 24 × 24 dp | `$--foreground-tertiary` | - |
| 我的 | lucide/user | 24 × 24 dp | - | `$--primary` |

**TabItem 标签**
| 属性 | 值 |
|------|-----|
| 字体 | `$--font-secondary` |
| 字号 | 10 sp |
| 未选中颜色 | `$--foreground-tertiary` |
| 选中颜色 | `$--primary` |
| 选中字重 | 500 (Medium) |

---

## 验收标准

### UI 验收（视觉还原）
- [ ] 页面背景色使用 `$--background` 变量
- [ ] 所有卡片背景使用 `$--card` 变量，圆角 16 dp
- [ ] 卡片阴影效果：offset(0, 2), blur=8, color=#00000008
- [ ] 头像 64×64 dp，圆形（圆角 32），默认渐变填充
- [ ] 统计数值使用不同强调色（primary/accent/success）
- [ ] 菜单图标使用对应颜色（书签-primary，记录-accent，下载-success，夜间-#8B5CF6）
- [ ] 分割线高度 1 dp，颜色 `$--border-light`
- [ ] 底部 TabBar 高度 83 dp，顶部有 1 dp 边框
- [ ] 当前 Tab（我的）使用 `$--primary` 高亮
- [ ] 所有文本字体为 Inter，字号字重符合设计规格

### 功能验收
- [ ] 用户卡片点击跳转个人资料页（本期 Toast）
- [ ] 设置图标点击跳转设置页（本期 Toast）
- [ ] 我的书签点击跳转书签列表页（本期 Toast）
- [ ] 阅读记录点击跳转阅读记录页（本期 Toast）
- [ ] 离线下载点击跳转离线管理页（本期 Toast）
- [ ] 夜间模式点击立即切换主题，状态持久化
- [ ] 书架 Tab 点击导航到书架页面
- [ ] 书城 Tab 点击跳转书城页面（本期 Toast）

### 数据验收
- [ ] 已读书籍数从 Room 计算（status=READ）
- [ ] 阅读小时数从 Room 累计（ReadingSession.duration）
- [ ] 收藏书籍数从 Room 计算（isFavorite=true）
- [ ] 数据为 0 时正常显示 "0"

### 边缘情况
- [ ] 未登录时显示默认头像和默认昵称
- [ ] 网络失败不影响本地统计展示（离线优先）

---

## 交互说明

| 入口 | 行为 |
|------|------|
| 用户卡片点击 | 跳转个人资料页（本期 Toast："个人资料功能开发中"） |
| 设置图标点击 | 跳转设置页（本期 Toast："设置功能开发中"） |
| 我的书签点击 | 跳转书签列表页（本期 Toast："书签功能开发中"） |
| 阅读记录点击 | 跳转阅读记录页（本期 Toast："阅读记录功能开发中"） |
| 离线下载点击 | 跳转离线管理页（本期 Toast："离线下载功能开发中"） |
| 夜间模式点击 | 切换深色/浅色主题，保存到 DataStore |
| 书架 Tab 点击 | 导航到书架页面 |
| 书城 Tab 点击 | 跳转书城页面（本期 Toast："书城功能开发中"） |

---

## 不在本期范围内

- 用户登录/注册功能
- 个人资料编辑功能
- 书签列表页面实现
- 阅读记录页面实现
- 离线下载管理页面实现
- 书城页面实现
- 设置页面实现
- 头像上传/自定义头像

---

## 技术约束

### 数据来源
| 数据 | 来源 | 说明 |
|------|------|------|
| 用户昵称/ID | DataStore（默认值） | 本期使用 Mock 数据，不实现登录 |
| 已读书籍数 | Room（Book 表） | 统计 status = READ 的记录数 |
| 阅读小时数 | Room（ReadingSession 表） | 累计 duration 字段（分钟转小时） |
| 收藏书籍数 | Room（Book 表） | 统计 isFavorite = true 的记录数 |
| 夜间模式状态 | DataStore | 布尔值，key: "dark_mode" |

### 复用组件
- `TabBar`：底部导航栏（已存在于设计系统，ID: t7yOu）

### 主题切换实现
- 使用 `AppTheme` 的 `darkTheme` 参数控制
- 通过 `CompositionLocal` 传递主题状态
- 切换时触发整体 UI 重组

---

## UI 结构树

```
ProfileScreen (375×812, fill=background, layout=vertical)
├── Header (44dp, padding=[0,20], spaceBetween)
│   ├── Title "我的" (28sp, Bold, foreground)
│   └── Icon settings (22×22, foreground)
│
├── Content (fillContainer, padding=20, gap=20, layout=vertical)
│   ├── UserCard (fillContainer, padding=20, cornerRadius=16, fill=card, gap=16)
│   │   ├── Avatar (64×64, cornerRadius=32, gradient)
│   │   ├── ProfileInfo (fillContainer, gap=4, layout=vertical)
│   │   │   ├── Name "阅读爱好者" (18sp, SemiBold, foreground)
│   │   │   └── ID "ID: 12345678" (13sp, foreground-secondary)
│   │   └── Icon chevron-right (20×20, foreground-tertiary)
│   │
│   ├── StatsCard (fillContainer, padding=20, cornerRadius=16, fill=card, spaceBetween)
│   │   ├── Stat1 (fillContainer, gap=4, center)
│   │   │   ├── Value "128" (24sp, Bold, primary)
│   │   │   └── Label "已读书籍" (12sp, foreground-secondary)
│   │   ├── Stat2 (fillContainer, gap=4, center)
│   │   │   ├── Value "256" (24sp, Bold, accent)
│   │   │   └── Label "阅读小时" (12sp, foreground-secondary)
│   │   └── Stat3 (fillContainer, gap=4, center)
│   │       ├── Value "32" (24sp, Bold, success)
│   │       └── Label "收藏书籍" (12sp, foreground-secondary)
│   │
│   └── MenuSection (fillContainer, padding=[0,16], cornerRadius=16, fill=card, layout=vertical)
│       ├── MenuItem1 (52dp, fillContainer, spaceBetween)
│       │   ├── Left (gap=12, center)
│       │   │   ├── Icon bookmark (20×20, primary)
│       │   │   └── Text "我的书签" (15sp, foreground)
│       │   └── Icon chevron-right (18×18, foreground-tertiary)
│       ├── Divider (1dp, fillContainer, border-light)
│       ├── MenuItem2 (52dp, fillContainer, spaceBetween)
│       │   ├── Left (gap=12, center)
│       │   │   ├── Icon history (20×20, accent)
│       │   │   └── Text "阅读记录" (15sp, foreground)
│       │   └── Icon chevron-right (18×18, foreground-tertiary)
│       ├── Divider (1dp, fillContainer, border-light)
│       ├── MenuItem3 (52dp, fillContainer, spaceBetween)
│       │   ├── Left (gap=12, center)
│       │   │   ├── Icon download (20×20, success)
│       │   │   └── Text "离线下载" (15sp, foreground)
│       │   └── Icon chevron-right (18×18, foreground-tertiary)
│       ├── Divider (1dp, fillContainer, border-light)
│       └── MenuItem4 (52dp, fillContainer, spaceBetween)
│           ├── Left (gap=12, center)
│           │   ├── Icon moon (20×20, #8B5CF6)
│           │   └── Text "夜间模式" (15sp, foreground)
│           └── Icon chevron-right (18×18, foreground-tertiary)
│
└── TabBar (83dp, fillContainer, fill=card, padding=[8,24,24,24], spaceBetween)
    ├── TabItem-Home (48×48, gap=4, center)
    │   ├── Icon book-open (24×24, foreground-tertiary)
    │   └── Label "书架" (10sp, foreground-tertiary)
    ├── TabItem-Store (48×48, gap=4, center)
    │   ├── Icon compass (24×24, foreground-tertiary)
    │   └── Label "书城" (10sp, foreground-tertiary)
    └── TabItem-Me (48×48, gap=4, center) [SELECTED]
        ├── Icon user (24×24, primary)
        └── Label "我的" (10sp, Medium, primary)
```

---

## 相关文件

- 设计稿：`pencil-new.pen` → `Profile-Me` 页面 (ID: pCCEE)
- 参考模板：`docs/templates/viewmodel.md`
