package funny.buildapp.progress.ui.page.plan

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(private val repo: PlanRepository) :
    BaseViewModel<PlanAction>() {

    private val _uiState = MutableStateFlow(PlanUiState())
    val uiState = _uiState

    override fun dispatch(action: PlanAction) {
        when (action) {
            is PlanAction.SendEvent -> _event.sendEvent(action.event)
            is PlanAction.GetAll -> getAll()
        }
    }

    init {
        getAll()
    }

    private fun getAll() {
        fetchData(
            request = { repo.getAll() },
            onSuccess = { _uiState.setState { copy(plans = it) } },
            onFailed = { _event.sendEvent(DispatchEvent.ShowToast(it)) }
        )
    }

}


data class PlanUiState(
    val plans: List<Plan> = emptyList(),
)


sealed class PlanAction {
    class SendEvent(val event: DispatchEvent) : PlanAction()
    object GetAll : PlanAction()
}