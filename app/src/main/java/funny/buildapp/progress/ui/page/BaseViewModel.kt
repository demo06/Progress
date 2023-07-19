package funny.buildapp.progress.ui.page

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
        this.value = this.value.reducer()
    }
}

fun DispatchEvent.executeEvent(
    context: Context,
    navController: NavHostController,
) {
    when (this) {
        is DispatchEvent.ShowToast -> ""
        is DispatchEvent.GoBack -> ""
        else -> {}
    }
}

sealed class DispatchEvent {
    object GoBack : DispatchEvent()
    class ShowToast(val msg: String) : DispatchEvent()
}
