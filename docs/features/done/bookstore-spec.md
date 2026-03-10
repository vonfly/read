# 书城（发现页）Spec

## 要解决的问题
用户需要一个在线书城入口，发现新书、浏览热门榜单、参与限时活动。

## 用户故事
- 作为读者，我可以浏览热门书籍榜单，以便发现好书
- 作为读者，我可以搜索书名或作者，以便快速找到想要的书籍
- 作为读者，我可以查看限时活动 Banner，以便获取免费或优惠书籍
- 作为读者，我可以按分类筛选书籍，以便找到感兴趣的类型

## 验收标准（完成的定义）

### 1. 页面布局
- [ ] 页面顶部显示标题"书城"（28sp, Bold, Foreground）
- [ ] 标题右侧显示搜索图标（22x22dp, Lucide search）
- [ ] 底部显示 TabBar，"书城" Tab 处于选中状态（Primary 色高亮）
- [ ] 背景色使用 `$--background` (#F9F9F7)
- [ ] 内容区域左右边距 20dp，顶部边距 16dp

### 2. 搜索栏
- [ ] 搜索栏高度 40dp，圆角 10dp
- [ ] 背景色 #F2F2F7
- [ ] 左侧显示搜索图标（18x18dp, Foreground-Tertiary）
- [ ] 占位文字"搜索书名、作者"（15sp, Normal, Foreground-Tertiary）
- [ ] 点击搜索栏跳转到搜索页面（本期可仅显示 Toast）

### 3. 分类标签
- [ ] 横向滚动，标签间距 8dp
- [ ] 每个标签高度 32dp，圆角 16dp（全圆角）
- [ ] 选中状态：背景 Primary 色，文字白色（14sp, Medium）
- [ ] 未选中状态：背景 #F2F2F7，文字 Foreground 色（14sp, Normal）
- [ ] 默认选中"推荐"
- [ ] 标签内容：推荐、小说、文学、历史、科技（可配置）
- [ ] 点击标签切换选中状态，刷新对应标签的测试数据

### 4. 活动 Banner
- [ ] Banner 高度 140dp，圆角 16dp
- [ ] 背景渐变：#667eea → #764ba2（90° 垂直渐变）
- [ ] 左侧显示：
  - 标题（如"限时免费"）：24sp, Bold, 白色
  - 描述（如"精选好书限时免费阅读"）：14sp, Normal, #FFFFFFCC
  - 标题与描述间距 8dp
- [ ] 右侧显示按钮"立即查看"：
  - 高度 36dp，圆角 18dp（全圆角）
  - 背景白色，文字 #667eea（14sp, SemiBold）
  - 水平内边距 20dp
- [ ] Banner 与搜索栏间距 20dp
- [ ] 点击 Banner 跳转活动页（本期可仅显示 Toast）
- [ ] Banner 轮播（多张 Banner）

### 5. 热门榜单
- [ ] 标题区域：
  - 左侧"热门榜单"（18sp, SemiBold, Foreground）
  - 右侧"更多" + 右箭头图标（14sp, Normal + 16x16dp 图标, Foreground-Secondary）
  - 标题行高度 24dp
- [ ] 书籍列表：
  - 每行一本书，高度 80dp，圆角 12dp，背景 Card 色
  - 内边距 12dp，行间距 12dp
- [ ] 书籍行内容（从左到右）：
  - 排名数字：20sp, Bold, Accent 色（如 #FF6B6B）
  - 书籍封面：48x64dp，圆角 4dp，渐变背景
  - 书籍信息（flex=1，垂直居中）：
    - 书名：15sp, SemiBold, Foreground，单行省略
    - 作者：12sp, Normal, Foreground-Secondary，单行省略
    - 评分与阅读量：11sp, Normal, Foreground-Tertiary（如"9.2分 · 128万人读过"）
  - 排名、封面、信息之间间距 12dp
- [ ] 点击书籍行跳转书籍详情页
- [ ] 点击"更多"跳转完整榜单页（本期可仅显示 Toast）

### 6. 空状态与加载
- [ ] 首次加载显示骨架屏或 Loading 指示器
- [ ] 网络错误显示错误提示 + 重试按钮
- [ ] 热门榜单为空时显示空状态占位

### 7. 响应式适配
- [ ] 内容区域使用 LazyColumn，支持垂直滚动
- [ ] 分类标签超出屏幕宽度时可横向滚动
- [ ] 适配不同屏幕尺寸（最小宽度 375dp）

## 不在本期范围内
- 搜索功能实现（仅 UI 展示）
- 更多榜单页面
- 书籍详情页
- 真实后端 API 对接（使用 Mock 数据）

## 技术约束
- UI 层：`ui/screen/bookstore/`
- 数据层：本期使用本地 Mock 数据，后续替换为 Retrofit API
- 复用已有组件：TabBar、SectionHeader
- 新增组件：SearchBar、CategoryChips、BannerCard、BookRankItem
- 遵循 Clean Architecture 分层
- ViewModel 使用 UiState + UiEvent 模式

## 设计规范参考

| 元素 | 规格 |
|------|------|
| 页面背景 | #F9F9F7 |
| 卡片背景 | #FFFFFF |
| 搜索栏背景 | #F2F2F7 |
| 标签选中背景 | #007AFF (Primary) |
| 标签未选中背景 | #F2F2F7 |
| Banner 渐变 | #667eea → #764ba2 |
| 排名数字颜色 | #FF6B6B (Accent) |
| 标题字号 | 28sp Bold |
| 章节标题 | 18sp SemiBold |
| 正文 | 15sp |
| 辅助文字 | 14sp / 12sp / 11sp |
| 圆角（大） | 16dp |
| 圆角（中） | 12dp |
| 圆角（小） | 10dp |
| 标签圆角 | 16dp (全圆角) |
| 按钮圆角 | 18dp (全圆角) |
| 内容边距 | 20dp |
| 元素间距 | 20dp / 12dp / 8dp |

## Mock 数据示例

```kotlin
// Banner
data class Banner(
    val title: String = "限时免费",
    val description: String = "精选好书限时免费阅读",
    val buttonText: String = "立即查看",
    val gradientStart: String = "#667eea",
    val gradientEnd: String = "#764ba2"
)

// 热门书籍
data class HotBook(
    val id: String,
    val rank: Int,
    val title: String,
    val author: String,
    val rating: Float,
    val readCount: String,  // "128万人读过"
    val coverGradientStart: String,
    val coverGradientEnd: String
)

// 分类
val categories = listOf("推荐", "小说", "文学", "历史", "科技")
```
