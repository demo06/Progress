package funny.buildapp.progress.ui.page

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import funny.buildapp.progress.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


abstract class BaseViewModel<T> : ViewModel() {

    protected val _event = MutableSharedFlow<DispatchEvent>()
    val mainEvent = _event.asSharedFlow()

    abstract fun dispatch(action: T)


    fun <T> MutableSharedFlow<T>.sendEvent(event: T) {
        viewModelScope.launch {
            this@sendEvent.emit(event)
        }
    }

    fun Any?.toast() {
        _event.sendEvent(DispatchEvent.ShowToast(this.toString()))
    }

    fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
        this.value = this.value.reducer()
    }

    protected fun <T> fetchData(
        request: suspend () -> T,
        onFailed: (String) -> Unit = {},
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            request
                .asFlow()
                .flowOn(Dispatchers.Default)
                .catch {
                    onFailed("获取信息失败")
                }
                .collect {
                    if (it == null) onFailed("获取信息失败")
                    else onSuccess(it)
                }
        }
    }

//    fun DispatchEvent.executeEvent() {
//        when (this) {
//            is DispatchEvent.ShowToast -> msg.showToast()
//            is DispatchEvent.Back -> {
//
//            }
//        }
//    }
}


sealed class DispatchEvent {
    class ShowToast(val msg: String) : DispatchEvent()
    object Back : DispatchEvent()
}
