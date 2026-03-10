# CLAUDE.md — 阅读器 App

## 项目基础信息

```
applicationId : com.vonfly.read
minSdk        : 26
targetSdk     : 36
包名根目录      : com/vonfly/read/
```

## 架构约定

通用规范由 `Android Developer` agent 保证，此处只补充本项目的额外要求：

- **分层**：`data` → `domain` → `ui` → `di` → `core`（跨层工具类/扩展函数，禁止混入业务层）
- **离线优先**：Room 是唯一数据真实来源，UI 只观察 Room 的 Flow；网络数据必须先写入 Room，再由 Room 通知 UI，禁止网络响应直接推送给 UI
- **版本管理**：所有依赖版本从 `gradle/libs.versions.toml` 引用，禁止在代码或 `build.gradle.kts` 中硬编码版本号

## 项目特殊约定

> 以下是本项目独有的规则，AI 生成代码必须遵守。根据实际情况填写，删除不适用的条目。

```
# 数据
# - 书籍 ID 格式：UUID v4 字符串
# - 电子书格式支持：EPUB（epublib）、TXT（自研解析）、PDF（PdfRenderer）

# 网络
# - 所有请求 Header 携带 Authorization token（见 NetworkModule）
# - 错误模型：使用 AppError sealed interface，见 @docs/conventions.md#apperror

# 功能约定
# - 分页统一用 Paging 3，不自实现
# - 阅读进度前台翻页时协程直写 Room（500ms 防抖），进入后台时 WorkManager 同步服务端

# DataStore 命名（全局唯一，防止多实例导致 IllegalStateException）
# - 通用偏好（界面语言、通知等）：@AppDataStore → 文件名 "app_settings"
# - 阅读器设置（字体、主题等）：@ReaderDataStore → 文件名 "reader_settings"
# - 两个实例均在 DataStoreModule 中声明，通过 @Qualifier 区分注入
# - 禁止在项目中声明第三个 DataStore 实例，新偏好必须复用以上两个
```

## Agent 路由规则

涉及以下任务时委派给 `Android Developer` agent，不要自己处理：
- 创建或修改 `.kt` 文件
- 修改 `libs.versions.toml`
- 调试 Android 编译错误 / Gradle 问题
- 审查 Compose UI 或 ViewModel 架构

## 延伸文档（按需读取）

| 需要做什么 | 读取哪个文件 |
|-----------|------------|
| **从零初始化项目** | @docs/setup.md |
| **开发新功能的完整流程** | @docs/workflow.md |
| 新增依赖格式 / Kotlin 2.0 插件配置 | @docs/versions.md |
| ViewModel / Screen / Content 写法 | @docs/templates/viewmodel.md |
| Repository / Room / DataStore 写法 | @docs/templates/data-layer.md |
| Navigation / Hilt Module / NetworkModule / DataStoreModule | @docs/templates/infrastructure.md |
| 书架列表分页（Paging 3） | @docs/templates/paging.md |
| 阅读器主题 / 字体 / 进度 / 常亮 / 长文本渲染 | @docs/templates/reader.md |
| 低频场景（EntryPoint / 沉浸式 / UseCase / AppError）| @docs/conventions.md |

---

> 📌 维护提示：修改此文件或任意 `docs/` 文档后，请同步更新下方日期，并确认对应文件仍然存在。

*最后更新：2026-03-10*
