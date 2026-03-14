# 书签页面 Spec

## 要解决的问题
用户需要一个书签管理页面，查看和管理阅读过程中添加的书签，快速定位到感兴趣的阅读位置。

## 验收标准
- [ ] 显示所有书籍的书签列表，按时间倒序排列
- [ ] 每个书签卡片显示：封面、书名、作者·章节、预览文本、创建时间
- [ ] 点击书签卡片跳转到对应书籍的对应位置
- [ ] 点击返回按钮返回"我的"页面
- [ ] 空状态显示占位图和提示文字
- [ ] 长按书签卡片显示删除选项（本期可暂不实现，显示 Toast 即可）

## 交互说明
- 点击返回：返回"我的"页面
- 点击书签卡片：跳转到阅读器对应位置（本期显示 Toast "跳转到阅读器功能开发中"）
- 长按书签卡片：显示删除确认（本期显示 Toast "删除功能开发中"）

## 不在本期范围内
- 书签编辑功能
- 书签分类/筛选
- 书签搜索
- 批量删除

## 技术约束
- **数据来源**：Mock 数据（暂不实现 Room 持久化）
- **复用组件**：无特殊复用组件
- **阴影效果**：参考 Profile 页面的 `CardShadowColor`（7% 不透明度）

---

## UI 结构树

```
Profile-Bookmarks (375 x 812)
├── Header (height: 44, padding: 0 20)
│   ├── backBtn (chevron-left, 24x24)
│   ├── title ("我的书签", 17sp, 600)
│   └── spacer (24x24)
├── Content (vertical, gap: 12, padding: 16 20)
│   ├── bookmark1 (height: 96, cornerRadius: 12, fill: $--card, padding: 12, gap: 12)
│   │   ├── cover (52x72, cornerRadius: 4, gradient)
│   │   └── info (vertical, gap: 2, fill_container)
│   │       ├── title ("三体", 15sp, 600, $--foreground)
│   │       ├── meta ("刘慈欣 · 第12章", 12sp, normal, $--foreground-tertiary)
│   │       ├── preview ("汪淼看着...", 13sp, normal, $--foreground-secondary, lineHeight: 1.3)
│   │       └── time ("2024-01-15 14:30", 11sp, normal, $--foreground-tertiary)
│   ├── bookmark2 (同上)
│   └── bookmark3 (同上)
└── profileTabBar (ref: t7yOu)
```

---

## 设计规格

### 页面
| 属性 | 值 |
|------|-----|
| 宽度 | 375dp (fill_container) |
| 高度 | 812dp (fill_container) |
| 背景 | $--background |
| 布局 | 垂直 |

### Header
| 属性 | 值 |
|------|-----|
| 高度 | 44dp |
| 宽度 | fill_container |
| 水平排列 | space_between |
| 垂直对齐 | center |
| 水平内边距 | 20dp |

### Header - backBtn
| 属性 | 值 |
|------|-----|
| 图标 | lucide:chevron-left |
| 尺寸 | 24x24dp |
| 颜色 | $--foreground |

### Header - title
| 属性 | 值 |
|------|-----|
| 内容 | "我的书签" |
| 字体 | Inter |
| 字号 | 17sp |
| 字重 | 600 (SemiBold) |
| 颜色 | $--foreground |

### Header - spacer
| 属性 | 值 |
|------|-----|
| 尺寸 | 24x24dp |

### Content
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 间距 | 12dp |
| 宽度 | fill_container |
| 高度 | fill_container |
| 水平内边距 | 20dp |
| 垂直内边距 | 16dp |

### Bookmark Card
| 属性 | 值 |
|------|-----|
| 宽度 | fill_container |
| 高度 | 96dp |
| 圆角 | 12dp |
| 背景 | $--card |
| 阴影 | 7% 不透明度黑色（参考 Profile 页面） |
| 布局 | 水平 |
| 间距 | 12dp |
| 内边距 | 12dp |

### Bookmark - Cover
| 属性 | 值 |
|------|-----|
| 宽度 | 52dp |
| 高度 | 72dp |
| 圆角 | 4dp |
| 背景 | 渐变（不同书籍不同颜色） |

**渐变色示例：**
- 书籍1：#11998e → #38ef7d (绿色)
- 书籍2：#667eea → #764ba2 (紫色)
- 书籍3：#ee0979 → #ff6a00 (橙红色)

### Bookmark - Info
| 属性 | 值 |
|------|-----|
| 布局 | 垂直 |
| 间距 | 2dp |
| 宽度 | fill_container |
| 高度 | fill_container |
| 垂直对齐 | center |

### Info - title
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 15sp |
| 字重 | 600 (SemiBold) |
| 颜色 | $--foreground |

### Info - meta
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 12sp |
| 字重 | normal |
| 颜色 | $--foreground-tertiary |
| 格式 | "{作者} · 第{章节}章" |

### Info - preview
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 13sp |
| 字重 | normal |
| 颜色 | $--foreground-secondary |
| 行高 | 1.3 |
| 宽度 | fill_container |
| 截断 | 省略号 |

### Info - time
| 属性 | 值 |
|------|-----|
| 字体 | Inter |
| 字号 | 11sp |
| 字重 | normal |
| 颜色 | $--foreground-tertiary |
| 格式 | "YYYY-MM-DD HH:mm" |

---

## 颜色变量

| 变量名 | 用途 |
|--------|------|
| $--background | 页面背景 |
| $--card | 卡片背景 |
| $--foreground | 主文字颜色 |
| $--foreground-secondary | 次要文字颜色 |
| $--foreground-tertiary | 三级文字颜色 |

---

## UI 验收标准（对照设计稿）

1. **Header**
   - [ ] 返回按钮图标为 chevron-left
   - [ ] 标题为"我的书签"，字号 17sp，字重 600
   - [ ] Header 高度为 44dp

2. **书签卡片**
   - [ ] 卡片高度 96dp，圆角 12dp
   - [ ] 封面尺寸 52x72dp，圆角 4dp
   - [ ] 封面使用渐变背景
   - [ ] 标题字号 15sp，字重 600
   - [ ] 元信息格式为"作者 · 第X章"
   - [ ] 预览文本字号 13sp，行高 1.3
   - [ ] 时间字号 11sp
   - [ ] 卡片间距 12dp
   - [ ] 卡片内边距 12dp

3. **阴影**
   - [ ] 书签卡片阴影为 7% 不透明度黑色
