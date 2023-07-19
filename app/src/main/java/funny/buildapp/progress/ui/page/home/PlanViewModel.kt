package funny.buildapp.progress.ui.page.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import funny.buildapp.progress.utils.loge
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(private val repo: PlanRepository) :
    BaseViewModel<PlanAction>() {

    suspend fun insert() {
        val state = repo.delete(3)
        state.loge()
    }

    override fun dispatch(action: PlanAction) {
        when (action) {
            is PlanAction.SendEvent -> {}
            is PlanAction.Save -> savePlan(action.plan)
            is PlanAction.Delete -> {}
            else -> {}
        }
    }

    /**
     * Save plan detail
     *
     * @param plan
     */
    private fun savePlan(plan: Plan) {
        viewModelScope.launch {
            repo.upsert(plan)
        }
    }

    private fun delPlan(id: Long) {
        viewModelScope.launch {
            flow<Long> { repo.delete(id) }
                .collect {

                }
        }
    }
}

sealed class PlanAction {

    class SendEvent(val event: DispatchEvent) : PlanAction()
    class Save(val plan: Plan) : PlanAction()
    class Delete(val id: Long) : PlanAction()

}