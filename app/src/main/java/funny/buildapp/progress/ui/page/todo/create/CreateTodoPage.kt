package funny.buildapp.progress.ui.page.todo.create

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.ui.page.DispatchEvent
import funny.buildapp.progress.ui.page.home.newPlan.NewPlanAction
import funny.buildapp.progress.ui.page.home.newPlan.PlanTitle
import funny.buildapp.progress.ui.page.home.plan.ProgressCard
import funny.buildapp.progress.ui.page.home.newPlan.TaskItem
import funny.buildapp.progress.ui.page.route.Route
import funny.buildapp.progress.ui.page.route.RouteUtils
import funny.buildapp.progress.ui.page.route.RouteUtils.back
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.cyan
import funny.buildapp.progress.ui.theme.red
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.utils.compareDate
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.daysBetweenDates
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.utils.showToast
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.CustomBottomSheet
import funny.buildapp.progress.widgets.FillWidthButton
import funny.buildapp.progress.widgets.MyDatePicker
import funny.buildapp.progress.widgets.RoundCard
import funny.buildapp.progress.widgets.SpaceLine
import funny.buildapp.progress.widgets.SwitchButton
import kotlinx.coroutines.launch
import java.nio.file.Files.delete


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTodoPage(
    navCtrl: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    viewModel: CreateScheduleViewModel = hiltViewModel(),
) {
    val id = navBackStackEntry?.arguments?.getInt("id") ?: 0
    val uiState by viewModel.uiState.collectAsState()
    var openDialog by remember { mutableStateOf(false) }
    var dialogState by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        viewModel.dispatch(CreateScheduleAction.GetTodoDetail(id = id))
        viewModel.mainEvent.collect {
            when (it) {
                is DispatchEvent.ShowToast -> it.msg.showToast()
                is DispatchEvent.Back -> navCtrl.back()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(backgroundGradient),
        ) {
            item {
                AppToolsBar(
                    title = if (id != 0) "编辑日程" else "添加日程",
                    tint = AppTheme.colors.themeUi,
                    backgroundColor = transparent,
                    onBack = { navCtrl.back() },
                )
            }
            item {
                PlanTitle(
                    text = uiState.title,
                    hint = "请在这里输入日程内容",
                    title = "日程内容",
                    onTextChange = {
                        viewModel.dispatch(CreateScheduleAction.SetTitle(it))
                    })
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    text = "日程设置",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            item {
                RoundCard {
                    TaskItem("执行时间", uiState.startDate) {
                        dialogState = 0
                        openDialog = !openDialog
                    }
                    SpaceLine()
                    TaskItem("结束时间", uiState.targetDate) {
                        dialogState = 1
                        openDialog = !openDialog
                    }
                    SpaceLine()
                    TaskItem("关联计划", content = {
                        SwitchButton(
                            modifier = Modifier.height(25.dp),
                            checked = uiState.isAssociatePlan,
                            onCheckedChange = {
                                viewModel.dispatch(CreateScheduleAction.SetAssociateState)
                            })
                    })
                }
            }
            item {
                Column {
                    AnimatedVisibility(visible = uiState.isAssociatePlan) {
                        Column {
                            RoundCard {
                                TaskItem("是否在计划内重复", content = {
                                    SwitchButton(
                                        modifier = Modifier.height(25.dp),
                                        checked = uiState.repeatable,
                                        onCheckedChange = {
                                            viewModel.dispatch(
                                                CreateScheduleAction.SetIsRepeat
                                            )
                                        })
                                })
                            }
                            if (uiState.associateId != 0) {
                                RoundCard {
                                    TaskItem(
                                        uiState.planTitle,
                                        content = {
                                            Text(
                                                text = "当前进度：${uiState.progress}%",
                                                fontSize = 14.sp,
                                                color = AppTheme.colors.themeUi,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                        },
                                        onItemClick = { viewModel.dispatch(CreateScheduleAction.GetPlans) })
                                }
                            }
                            RoundCard {
                                TaskItem(
                                    "选择计划...",
                                    content = { },
                                    onItemClick = { viewModel.dispatch(CreateScheduleAction.GetPlans) })
                            }
                        }
                    }
                }
            }
            item {
                FillWidthButton(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = "保存"
                ) {
                    viewModel.dispatch(CreateScheduleAction.Save)
                }
            }
            if (id != 0) {
                item {
                    FillWidthButton(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        text = "删除",
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colors.themeUi.copy(0.2f),
                        ),
                        fontColor = red.copy(0.6f),
                        onClick = {
                            viewModel.dispatch(CreateScheduleAction.Delete)
                        },
                    )
                }
            }
        }
        if (openDialog) {
            MyDatePicker(
                onDismiss = {
                    openDialog = !openDialog
                },
                onConfirm = {
                    if (dialogState == 0) {
                        viewModel.dispatch(CreateScheduleAction.SetStartDate(it))
                    } else {
                        viewModel.dispatch(CreateScheduleAction.SetTargetDate(it))
                    }
                })
        }
        CustomBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = uiState.planBottomSheet,
            content = {
                PlanBottomSheet(
                    onItemClick = { id, title, progress ->
                        viewModel.dispatch(CreateScheduleAction.SetPlan(id, title, progress))
                    },
                    onDismiss = { viewModel.dispatch(CreateScheduleAction.ChangeBottomSheet) },
                    plans = uiState.plans
                )
            })
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanBottomSheet(
    plans: List<Plan>,
    onItemClick: (Int, String, Double) -> Unit = { id, title, double -> },
    onDismiss: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(cyan),
        content = {
            stickyHeader {
                AppToolsBar(
                    title = "选择计划",
                    backgroundColor = cyan,
                    imageVector = Icons.Default.Close,
                    onRightClick = { onDismiss() },
                )
            }
            items(plans, key = { it.id }) {
                val percentage = it.initialValue.toDouble() / it.targetValue.toDouble() * 100
                ProgressCard(
                    progress = String.format("%.1f", percentage).toDouble(),
                    title = it.title,
                    status = when (it.status) {
                        0 -> "未开始"
                        1 -> "进行中"
                        2 -> "已完成"
                        else -> "未知"
                    },
                    lastDay = "${daysBetweenDates(getCurrentDate(), it.endDate.dateToString())}",
                    proportion = "${it.initialValue}/${it.targetValue}",
                    onClick = {
                        onItemClick(
                            it.id.toInt(),
                            it.title,
                            String.format("%.1f", percentage).toDouble()
                        )
                    })
            }
        })
}

