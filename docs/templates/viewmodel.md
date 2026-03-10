# 模板：ViewModel / Screen / Content

## ViewModel 完整模板

```kotlin
package com.yourcompany.yourapp.ui.screen.[feature]

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.yourapp.domain.usecase.GetXxxUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class XxxUiState(
    val isLoading: Boolean = false,
    val items: ImmutableList<XxxItem> = persistentListOf(),
    // 禁止在 UiState 放一次性事件字段
)

sealed class XxxUiEvent {
    data class ShowSnackbar(val message: String) : XxxUiEvent()
    data object NavigateBack : XxxUiEvent()
}

@HiltViewModel
class XxxViewModel @Inject constructor(
    private val getXxxUseCase: GetXxxUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(XxxUiState())
    val uiState: StateFlow<XxxUiState> = _uiState.asStateFlow()

    private val _event = Channel<XxxUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init { loadData() }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getXxxUseCase()
                .onSuccess { items ->
                    _uiState.update { it.copy(isLoading = false, items = items) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.send(XxxUiEvent.ShowSnackbar(error.message ?: "操作失败"))
                }
        }
    }

    // 用户交互统一入口，每个交互对应一个具名方法，不用单一 onAction 泛型
    fun onItemClick(id: String) {
        viewModelScope.launch {
            _event.send(XxxUiEvent.NavigateBack)
        }
    }

    fun onDeleteClick(id: String) {
        viewModelScope.launch {
            // 处理删除逻辑
        }
    }
}
```

## Screen + Content 完整模板

```kotlin
// [Feature]Screen.kt — 有状态层，不可 Preview
@Composable
fun XxxScreen(
    onNavigateBack: () -> Unit,
    viewModel: XxxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is XxxUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                XxxUiEvent.NavigateBack    -> onNavigateBack()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        XxxContent(
            uiState = uiState,
            onItemClick = viewModel::onItemClick,    // 具名方法引用，类型明确
            onDeleteClick = viewModel::onDeleteClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// [Feature]Content.kt — 无状态层，可 Preview
@Composable
fun XxxContent(
    uiState: XxxUiState,
    onItemClick: (String) -> Unit,    // 与 ViewModel 方法签名一致
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // UI 实现
}

@Preview(showBackground = true)
@Composable
private fun XxxContentPreview() {
    AppTheme {
        XxxContent(
            uiState = XxxUiState(isLoading = false),
            onItemClick = {},
            onDeleteClick = {}
        )
    }
}
```

> **注意**：Content 的回调参数类型必须与 ViewModel 对应方法签名完全一致，
> 否则方法引用 `viewModel::onItemClick` 会在编译期报类型不匹配错误。
