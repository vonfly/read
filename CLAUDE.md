# 轻阅

EPUB / TXT / PDF 阅读器，离线优先，Kotlin + Jetpack Compose + Room。

**Android · minSdk 26 · targetSdk 35 · 包名 com.vonfly.read**

---

## 常用命令

```bash
./gradlew assembleDebug          # 编译
./gradlew test                   # 单元测试
./gradlew connectedAndroidTest   # 仪器测试（需连接设备）
./gradlew lint                   # Lint 检查
```

---

## 架构约定（本项目强制，不可偏离）

### 分层结构
```
data/       local（Room Entity/DAO）、remote（Retrofit/DTO）、repository（实现类）
domain/     model（业务模型）、repository（接口）、usecase（单一 invoke）
ui/         screen/[feature]/、component/、navigation/、theme/
di/         Hilt Module
core/       扩展函数、工具类、基类（跨层复用，不属于任何业务层）
```

---

## 核心约束

1. **禁止凭记忆假设版本号**——先读 `gradle/libs.versions.toml`
2. **离线优先**：Room 是唯一数据真实来源，网络数据必须先写入 Room 再通知 UI
3. **所有请求 Header 携带 Authorization token**，错误统一用 `AppError` sealed interface

---

## 架构

Clean Architecture 三层分层，每个 Feature 三文件结构，状态管理用 UiState + UiEvent 双轨。
详细规范见 `@docs/templates/viewmodel.md`。

技术选型：Hilt · Room · Coroutines+Flow · DataStore · Coil · Retrofit+KotlinSerialization · Navigation 2.8+

---

## Agent 路由

涉及以下 Android 开发任务时，使用 Agent 工具并指定 `subagent_type: "Android"`：

- Kotlin 文件 (`.kt`) 的创建或修改
- Jetpack Compose UI / ViewModel / Room / Hilt / Navigation
- `libs.versions.toml` 依赖版本配置
- Android 编译错误修复

---

## 延伸文档

| 什么时候读 | 文档 |
|------------|------|
| 新增 Feature，或不确定从哪里开始 | @docs/workflow.md |
| 写 ViewModel、Screen、UiState、UiEvent | @docs/templates/viewmodel.md |
| 写 Repository、DAO、Room Entity、DataStore | @docs/templates/data-layer.md |
| 写 NavHost、Hilt Module、NetworkModule、DataStoreModule | @docs/templates/infrastructure.md |
| 书架或任何列表需要分页 | @docs/templates/paging.md |
| 写阅读器页面（翻页、字体、进度、长文本渲染） | @docs/templates/reader.md |
| 用到 UseCase、AppError、沉浸式、HiltEntryPoint | @docs/conventions.md |
| 从零初始化新项目 | @docs/setup.md |


> 📌 维护提示：修改此文件或任意 `docs/` 文档后，请同步更新下方日期，并确认对应文件仍然存在。

*最后更新：2026-03-12*
