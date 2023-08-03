package funny.buildapp.progress.ui.page.daily

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.daily.Daily
import funny.buildapp.progress.data.source.relation.DailyWithTodo
import funny.buildapp.progress.ui.page.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(private val todoRepo: TodoRepository) : BaseViewModel<DailyAction>() {

    private val _uiState = MutableStateFlow(DailyState())
    val uiState = _uiState

    override fun dispatch(action: DailyAction) {
        when (action) {
            is DailyAction.Load -> getDailyTodo()
            is DailyAction.UpTodayTask -> upsertDaily(action.daily)
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

data class DailyState(
    val todos: List<DailyWithTodo> = emptyList(),
)

sealed class DailyAction {

    object Load : DailyAction()
    class UpTodayTask(val daily: Daily) : DailyAction()
}