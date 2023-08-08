package funny.buildapp.progress.ui.page.todo

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.daily.Daily
import funny.buildapp.progress.data.source.relation.DailyWithTodo
import funny.buildapp.progress.ui.page.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepo: TodoRepository) : BaseViewModel<TodoAction>() {

    private val _uiState = MutableStateFlow(TodoState())
    val uiState = _uiState

    override fun dispatch(action: TodoAction) {
        when (action) {
            is TodoAction.Load -> getDailyTodo()
            is TodoAction.UpTodayTask -> upsertDaily(action.daily)
        }
    }

    private fun getDailyTodo() {
        fetchData(
            request = { todoRepo.getTodoFormDaily() },
            onSuccess = {
                _uiState.setState {
                    copy(todos = it)
                }
            }
        )
    }

    private fun upsertDaily(daily: Daily) {
        fetchData(
            request = { todoRepo.upsertDaily(daily.copy(state = !daily.state)) },
            onSuccess = {
                getDailyTodo()
            }
        )
    }
}

data class TodoState(
    val todos: List<DailyWithTodo> = emptyList(),
)

sealed class TodoAction {

    object Load : TodoAction()
    class UpTodayTask(val daily: Daily) : TodoAction()
}