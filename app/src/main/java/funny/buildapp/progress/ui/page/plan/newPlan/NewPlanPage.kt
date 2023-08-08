package funny.buildapp.progress.ui.page.plan.newPlan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import funny.buildapp.progress.ui.page.DispatchEvent
import funny.buildapp.progress.ui.route.RouteUtils.back
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.red
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.ui.theme.white
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.showToast
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.FillWidthButton
import funny.buildapp.progress.widgets.MyDatePicker
import funny.buildapp.progress.widgets.RoundCard
import funny.buildapp.progress.widgets.SpaceLine
import funny.buildapp.progress.widgets.SwitchButton
import funny.buildapp.progress.widgets.clickWithoutWave

@Composable
fun NewPlanPage(
    navCtrl: NavHostController,
    id: Int = 0,
    viewModel: NewPlanViewModel = hiltViewModel(),
    isEditMode: Boolean = false,
    onDismiss: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val plan = uiState.plan
    val snackState = remember { SnackbarHostState() }
    var dialogState by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        viewModel.dispatch(NewPlanAction.GetPlanDetail(id = id))
        viewModel.mainEvent.collect {
            when (it) {
                is DispatchEvent.ShowToast -> {
                    it.msg.showToast()
                }

                is DispatchEvent.Back -> {
                    navCtrl.back()
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .then(
                    if (isEditMode) {
                        Modifier
                            .padding(top = 60.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                    } else {
                        Modifier
                    }
                )
                .background(white)
                .background(backgroundGradient),
        ) {
            item {
                AppToolsBar(
                    title = if (isEditMode) "编辑计划" else "新建计划",
                    tint = AppTheme.colors.themeUi,
                    backgroundColor = transparent,
                    onBack = if (isEditMode) {
                        null
                    } else {
                        { navCtrl.back() }
                    },
                    imageVector = if (isEditMode) Icons.Default.Close else null,
                    onRightClick = if (isEditMode) {
                        { onDismiss?.invoke() }
                    } else {
                        null
                    }
                )
            }
            item {
                PlanTitle(
                    text = plan.title,
                    onTextChange = {
                        viewModel.dispatch(NewPlanAction.SetTitle(title = it))
                    })
            }
            item {
                DateCard(
                    startTime = plan.startDate.dateToString(),
                    endTime = plan.endDate.dateToString(),
                    startTimeClick = {
                        dialogState = 0
                        viewModel.dispatch(NewPlanAction.SetDialogState)
                    },
                    endTimeClick = {
                        dialogState = 1
                        viewModel.dispatch(NewPlanAction.SetDialogState)
                    })
            }
            item {
                ProportionCard(
                    initialValue = plan.initialValue.toString(),
                    targetValue = plan.targetValue.toString(),
                    adjustState = plan.autoAdjust,
                    adjustChange = {
                        viewModel.dispatch(NewPlanAction.SetAdjustState)
                    },
                    initialValueChange = {
                        viewModel.dispatch(NewPlanAction.SetInitialValue(it))
                    },
                    endValueChange = {
                        viewModel.dispatch(NewPlanAction.SetTargetValue(it))
                    })
            }
            item {
                FillWidthButton(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = "保存"
                ) {
                    viewModel.dispatch(NewPlanAction.Save)
                }
            }
            if (isEditMode) {
                item {
                    FillWidthButton(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = "删除",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.themeUi.copy(0.2f),
                        ),
                        fontColor = red.copy(0.6f),
                        onClick = {
                            viewModel.dispatch(NewPlanAction.Delete)
                        },
                    )
                }
            }

        }
        if (uiState.datePickerDialog) {
            MyDatePicker(
                isStartTime = dialogState == 0,
                onDismiss = {
                    viewModel.dispatch(NewPlanAction.SetDialogState)
                },
                onConfirm = {
                    viewModel.dispatch(
                        if (dialogState == 0) NewPlanAction.SetStartTime(it)
                        else NewPlanAction.SetEndTime(it)
                    )
                })
        }
        SnackbarHost(hostState = snackState, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun PlanTitle(
    text: String = "",
    hint: String = "在这里输入计划标题",
    title: String = "计划名称",
    onTextChange: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            RoundCard {
                BasicTextField(
                    value = text,
                    onValueChange = { onTextChange(it) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(color = AppTheme.colors.textPrimary),
                    cursorBrush = SolidColor(AppTheme.colors.themeUi),
                    decorationBox = @Composable { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                // 当空字符时, 显示hint
                                if (text.isEmpty())
                                    Text(
                                        text = hint,
                                        color = AppTheme.colors.textSecondary,
                                    )
                                // 原本输入框的内容
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun DateCard(
    startTime: String,
    endTime: String,
    startTimeClick: () -> Unit,
    endTimeClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = "计划时间",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            RoundCard {
                TaskItem("开始时间", startTime) { startTimeClick() }
                SpaceLine()
                TaskItem("结束时间", endTime) { endTimeClick() }
            }
        }
    )
}


@Composable
fun ProportionCard(
    initialValue: String,
    targetValue: String,
    adjustState: Boolean,
    adjustChange: () -> Unit = {},
    initialValueChange: (Int) -> Unit = {},
    endValueChange: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = "计划比例",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            RoundCard {
                TaskItem("根据任务量自动调整", content = {
                    SwitchButton(
                        modifier = Modifier.height(25.dp),
                        checked = adjustState,
                        onCheckedChange = { adjustChange() },
                    )
                })
                AnimatedVisibility(visible = !adjustState) {
                    Column {
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(AppTheme.colors.divider)
                        )
                        TaskItem("起始量", "0", content = {
                            RoundEditText(initialValue, onValueChange = { initialValueChange(it) })
                        })
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(AppTheme.colors.divider)
                        )
                        TaskItem("计划量", "100", content = {
                            RoundEditText(targetValue, onValueChange = { endValueChange(it) })
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun RoundEditText(value: String, onValueChange: (Int) -> Unit = {}) {
    BasicTextField(
        modifier = Modifier
            .width(90.dp)
            .background(
                AppTheme.colors.themeUi.copy(0.8f),
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(color = white, textAlign = TextAlign.Center),
        cursorBrush = SolidColor(white),
        onValueChange = {
            if (it.length < 10 && it.isDigitsOnly())
                onValueChange(
                    if (it.isNotBlank()) it.toInt() else 0
                )
        }
    )
}

@Composable
fun TaskItem(
    title: String,
    text: String = "",
    content: @Composable (() -> Unit?)? = null,
    onItemClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickWithoutWave { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        if (content == null) {
            Text(
                text = text,
                color = AppTheme.colors.textThird,
                modifier = Modifier
                    .width(90.dp)
                    .background(
                        AppTheme.colors.themeUi.copy(0.8f),
                        RoundedCornerShape(8.dp)
                    )
                    .clickWithoutWave { onClick() }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        } else {
            content()
        }

    }
}
