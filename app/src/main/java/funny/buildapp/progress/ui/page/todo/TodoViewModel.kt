package funny.buildapp.progress.ui.page.todo

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repo: TodoRepository) :
    BaseViewModel<TodoAction>() {

    private val _uiState = MutableStateFlow(TodoState())
    val uiState = _uiState
    override fun dispatch(action: TodoAction) {
        when (action) {
            is TodoAction.SendEvent -> _event.sendEvent(action.event)
            is TodoAction.GetTodoList -> getTodoList(action.date)
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

data class TodoState(
    val todos: List<Todo> = emptyList(),
)

sealed class TodoAction {
    class SendEvent(val event: DispatchEvent) : TodoAction()
    class GetTodoList(val date: Long) : TodoAction()

}

