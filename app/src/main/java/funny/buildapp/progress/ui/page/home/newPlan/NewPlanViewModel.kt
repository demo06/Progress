package funny.buildapp.progress.ui.page.home.newPlan

import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.ui.page.BaseViewModel
import funny.buildapp.progress.ui.page.DispatchEvent
import funny.buildapp.progress.utils.compareDate
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.getCurrentDate
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NewPlanViewModel @Inject constructor(private val repo: PlanRepository) :
    BaseViewModel<NewPlanAction>() {

    private val _uiState = MutableStateFlow(NewPlanUiState())
    val uiState = _uiState

    override fun dispatch(action: NewPlanAction) {
        when (action) {
            is NewPlanAction.SendEvent -> _event.sendEvent(action.event)
            is NewPlanAction.Save -> savePlan()
            is NewPlanAction.Delete -> deletePlan()
            is NewPlanAction.SetTitle -> setTitle(action.title)
            is NewPlanAction.SetStartTime -> setStartTime(action.time)
            is NewPlanAction.SetEndTime -> setEndTime(action.time)
            is NewPlanAction.SetInitialValue -> setInitialValue(action.value)
            is NewPlanAction.SetTargetValue -> setEndValue(action.value)
            is NewPlanAction.SetDialogState -> setDialogState()
            is NewPlanAction.GetPlanDetail -> getPlanDetail(action.id)
        }
    }


    private fun getPlanDetail(id: Int) {
        fetchData(
            request = { repo.getPlanDetail(id) },
            onSuccess = {
                _uiState.setState {
                    copy(
                        id = it.id,
                        title = it.title,
                        startTime = it.startDate,
                        endTime = it.endDate,
                        initialValue = it.initialValue,
                        targetValue = it.targetValue,
                        datePickerDialog = false
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
        if (!compareDate(
                _uiState.value.startTime.dateToString(),
                _uiState.value.endTime.dateToString()
            )
        ) {
            "结束时间不能早于开始时间".toast()
            return false
        }
        if (_uiState.value.initialValue > _uiState.value.targetValue) {
            "初始值不能大于目标值".toast()
            return false
        }
        return true
    }

    private fun savePlan() {
        if (!checkParams()) return
        fetchData(
            request = {
                repo.upsert(
                    Plan(
                        id = _uiState.value.id,
                        title = _uiState.value.title,
                        startDate = _uiState.value.startTime,
                        endDate = _uiState.value.endTime,
                        initialValue = _uiState.value.initialValue,
                        targetValue = _uiState.value.targetValue,
                        status = 0,
                    )
                )
            },
            onSuccess = {
                if (it > 0) {
                    "保存成功".toast()
                    _event.sendEvent(DispatchEvent.Back)
                } else {
                    "保存失败".toast()
                }
            },
            onFailed = {
                "保存失败".toast()
            }
        )
    }

    private fun deletePlan() {
        fetchData(
            request = { repo.delete(_uiState.value.id) },
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

    private fun setStartTime(time: Long) {
        _uiState.setState { copy(startTime = time) }
    }

    private fun setEndTime(time: Long) {
        _uiState.setState { copy(endTime = time) }
    }

    private fun setInitialValue(value: Int) {
        _uiState.setState { copy(initialValue = value) }

    }

    private fun setEndValue(value: Int) {
        _uiState.setState { copy(targetValue = value) }
    }

    private fun setDialogState() {
        _uiState.setState { copy(datePickerDialog = !_uiState.value.datePickerDialog) }
    }


}

data class NewPlanUiState(
    val id: Long = 0,
    val title: String = "",
    val startTime: Long = getCurrentDate(),
    val endTime: Long = getCurrentDate(),
    val initialValue: Int = 0,
    val targetValue: Int = 100,
    val datePickerDialog: Boolean = false,
)

sealed class NewPlanAction {
    class SendEvent(val event: DispatchEvent) : NewPlanAction()
    object Save : NewPlanAction()
    object Delete : NewPlanAction()
    class GetPlanDetail(val id: Int) : NewPlanAction()
    object SetDialogState : NewPlanAction()
    class SetTitle(val title: String) : NewPlanAction()
    class SetStartTime(val time: Long) : NewPlanAction()
    class SetEndTime(val time: Long) : NewPlanAction()
    class SetInitialValue(val value: Int) : NewPlanAction()
    class SetTargetValue(val value: Int) : NewPlanAction()

}