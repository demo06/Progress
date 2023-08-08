package funny.buildapp.progress.ui.page.schedule

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repo: TodoRepository) :
    BaseViewModel<ScheduleAction>() {

    private val _uiState = MutableStateFlow(ScheduleState())
    val uiState = _uiState
    override fun dispatch(action: ScheduleAction) {
        when (action) {
            is ScheduleAction.SendEvent -> _event.sendEvent(action.event)
            is ScheduleAction.GetScheduleList -> getTodoList(action.date)
        }
    }

    private fun getTodoList(date: Long) {
        fetchData(
            request = { repo.getTodoByDate(date) },
            onSuccess = {
                _uiState.setState {
                    copy(todos = it)
                }
            }
        )
    }


}

data class ScheduleState(
    val todos: List<Todo> = emptyList(),
)

sealed class ScheduleAction {
    class SendEvent(val event: DispatchEvent) : ScheduleAction()
    class GetScheduleList(val date: Long) : ScheduleAction()

}

