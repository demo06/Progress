package funny.buildapp.progress.ui.page.todo.create

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import funny.buildapp.progress.utils.compareDate
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.utils.stringToDate
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateScheduleViewModel @Inject constructor(
    private val repo: TodoRepository,
    private val planRepo: PlanRepository
) :
    BaseViewModel<CreateScheduleAction>() {

    private val _uiState = MutableStateFlow(CreateScheduleState())
    val uiState = _uiState

    override fun dispatch(action: CreateScheduleAction) {
        when (action) {
            is CreateScheduleAction.SendEvent -> _event.sendEvent(action.event)
            is CreateScheduleAction.ChangeBottomSheet -> setDialogState()
            is CreateScheduleAction.GetTodoDetail -> getTodoDetail(action.id)
            is CreateScheduleAction.GetPlans -> getPlanDetail()
            is CreateScheduleAction.Save -> saveSchedule()
            is CreateScheduleAction.Delete -> delete()
            is CreateScheduleAction.SetStartDate -> setStartDate(action.time)
            is CreateScheduleAction.SetTargetDate -> setTargetDate(action.time)
            is CreateScheduleAction.SetAssociateState -> setAssociateState()
            is CreateScheduleAction.SetIsRepeat -> setIsRepeat()
            is CreateScheduleAction.SetPlan -> setPlan(action.id, action.title, action.progress)
            is CreateScheduleAction.SetTitle -> setTitle(action.title)
        }
    }

    private fun setPlan(id: Int, title: String, progress: Double) {
        _uiState.setState {
            copy(
                associateId = id,
                planTitle = title,
                progress = progress,
                planBottomSheet = false
            )
        }
    }


    private fun getPlanDetail() {
        fetchData(
            request = { planRepo.getAll() },
            onSuccess = {
                _uiState.setState {
                    copy(
                        plans = it,
                        planBottomSheet = true
                    )
                }
            }
        )
    }

    private fun getTodoDetail(id: Int) {
        fetchData(
            request = { repo.getTodoById(id) },
            onSuccess = {
                _uiState.setState {
                    copy(
                        id = it.id.toInt(),
                        title = it.title,
                        startDate = it.startDate.dateToString(),
                        targetDate = it.endDate.dateToString(),
                        isAssociatePlan = it.isAssociatePlan,
                        associateId = it.associateId,
                        repeatable = it.repeatable
                    )
                }
            }
        )
    }

    private fun checkParams(): Boolean {
        if (_uiState.value.title.isEmpty()) {
            "标题不能为空".toast()
            return false
        }
        if (!compareDate(_uiState.value.startDate, _uiState.value.targetDate)) {
            "结束时间不能早于开始时间".toast()
            return false
        }
//        if (_uiState.value.isAssociatePlan && _uiState.value.associateId == 0) {
//            "请选择相关联的计划".toast()
//            return false
//        }
        return true
    }

    private fun saveSchedule() {
        if (!checkParams()) return
        fetchData(
            request = {
                repo.upsert(
                    Todo(
                        id = _uiState.value.id.toLong(),
                        title = _uiState.value.title,
                        startDate = _uiState.value.startDate.stringToDate(),
                        endDate = _uiState.value.targetDate.stringToDate(),
                        isAssociatePlan = _uiState.value.isAssociatePlan,
                        associateId = _uiState.value.associateId,
                        repeatable = _uiState.value.repeatable,
                        status = 0,
                    )
                )
            },
            onSuccess = {
                if (it > 0) {
                    _event.sendEvent(DispatchEvent.Back)
                    "保存成功".toast()
                } else {
                    "保存失败".toast()
                }
            },
            onFailed = {
                "保存失败".toast()
            }
        )
    }


    private fun delete() {
        fetchData(
            request = { repo.delete(_uiState.value.id.toLong()) },
            onSuccess = {
                if (it > 0) {
                    "删除成功".toast()
                    _event.sendEvent(DispatchEvent.Back)
                } else {
                    "删除失败".toast()
                }
            },
            onFailed = {
                "删除失败".toast()
            }
        )
    }


    private fun setTitle(title: String) {
        _uiState.setState { copy(title = title) }
    }

    private fun setStartDate(time: String) {
        _uiState.setState { copy(startDate = time) }
    }

    private fun setTargetDate(time: String) {
        _uiState.setState { copy(targetDate = time) }
    }

    private fun setAssociateState() {
        _uiState.setState { copy(isAssociatePlan = !_uiState.value.isAssociatePlan) }

    }

    private fun setIsRepeat() {
        _uiState.setState { copy(repeatable = !_uiState.value.repeatable) }
    }

    private fun setDialogState() {
        _uiState.setState { copy(planBottomSheet = !_uiState.value.planBottomSheet) }
        if (_uiState.value.planBottomSheet) {
            getTodoDetail(_uiState.value.associateId)
        }
    }


}


data class CreateScheduleState(
    val id: Int = 0,
    val title: String = "",
    val startDate: String = getCurrentDate(),
    val targetDate: String = getCurrentDate(),
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = false,
    val associateId: Int = 0,
    val progress: Double = 0.0,
    val planBottomSheet: Boolean = false,
    val planTitle: String = "",
    val plans: List<Plan> = emptyList(),
)

sealed class CreateScheduleAction {
    class SendEvent(val event: DispatchEvent) : CreateScheduleAction()
    object Save : CreateScheduleAction()
    object ChangeBottomSheet : CreateScheduleAction()
    object Delete : CreateScheduleAction()
    object GetPlans : CreateScheduleAction()
    class GetTodoDetail(val id: Int) : CreateScheduleAction()
    class SetTitle(val title: String) : CreateScheduleAction()
    class SetStartDate(val time: String) : CreateScheduleAction()
    class SetTargetDate(val time: String) : CreateScheduleAction()
    object SetAssociateState : CreateScheduleAction()
    object SetIsRepeat : CreateScheduleAction()
    class SetPlan(val id: Int, val title: String, val progress: Double) : CreateScheduleAction()
}
