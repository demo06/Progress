package funny.buildapp.progress.ui.page.home.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.utils.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PlanDetailViewModel @Inject constructor(
    private val repo: PlanRepository,
    private val todoRepo: TodoRepository
) :
    BaseViewModel<PlanDetailAction>() {

    private val _uiState = MutableStateFlow(PlanDetailState())
    val uiState = _uiState

    override fun dispatch(action: PlanDetailAction) {
        when (action) {
            is PlanDetailAction.GetPlanDetail -> getPlanDetail(action.planId)
            is PlanDetailAction.GetTodos -> getTodos(action.planId)
        }
    }

    private fun getPlanDetail(id: Int) {
        fetchData(
            request = { repo.getPlanDetail(id.toLong()) },
            onSuccess = {
                _uiState.setState { copy(plan = it) }
                dispatch(PlanDetailAction.GetTodos(id))
            }
        )
    }

    private fun getTodos(id: Int) {
        fetchData(
            request = { todoRepo.getTodoByPlanId(id) },
            onSuccess = { _uiState.setState { copy(todos = it) } }
        )
    }


}

data class PlanDetailState(
    val plan: Plan = Plan(0),
    val todos: List<Todo> = emptyList(),
)

sealed class PlanDetailAction {
    data class GetPlanDetail(val planId: Int) : PlanDetailAction()
    data class GetTodos(val planId: Int) : PlanDetailAction()
}