package funny.buildapp.progress.ui.page.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.page.home.PlanTitle
import funny.buildapp.progress.ui.page.home.TaskItem
import funny.buildapp.progress.ui.page.route.RouteUtils.back
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.utils.compareDate
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.FillWidthButton
import funny.buildapp.progress.widgets.MyDatePicker
import funny.buildapp.progress.widgets.RoundCard
import funny.buildapp.progress.widgets.SpaceLine
import funny.buildapp.progress.widgets.SwitchButton
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSchedulePage(navCtrl: NavHostController) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var bottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackState)
        },
        content = { it->it
            ScheduleBody(
                navCtrl = navCtrl,
                dialogConfirm = {
                    snackScope.launch { snackState.showSnackbar(it) }
                },
                selectPlan = {
                    bottomSheet = !bottomSheet
                })
        })
}

@Composable
fun ScheduleBody(
    navCtrl: NavHostController,
    dialogConfirm: (String) -> Unit,
    selectPlan: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(getCurrentDate()) }
    var endTime by remember { mutableStateOf(getCurrentDate()) }
    var dialogState by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(backgroundGradient),
        ) {
            item {
                AppToolsBar(
                    title = "添加日程",
                    tint = AppTheme.colors.themeUi,
                    backgroundColor = transparent,
                    onBack = { navCtrl.back() },
                )
            }
            item {
                PlanTitle(
                    text = title,
                    hint = "请在这里输入日程内容",
                    title = "日程内容",
                    onTextChange = {
                        title = it
                    })
            }
            item {
                ScheduleDateCard(
                    startTime = startTime,
                    endTime = endTime,
                    startTimeClick = {
                        dialogState = 0
                        openDialog = !openDialog
                    },
                    endTimeClick = {
                        dialogState = 1
                        openDialog = !openDialog
                    },
                    selectPlan = { selectPlan() })
            }
            item {
                FillWidthButton(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = "添加"
                ) {}
            }
        }
        if (openDialog) {
            MyDatePicker(
                onDismiss = {
                    openDialog = !openDialog
                },
                onConfirm = {
                    if (dialogState == 0) {
                        if (compareDate(startTime, it)) {
                            startTime = it
                        } else {
                            dialogConfirm("开始时间不能大于结束时间")
                        }
                    } else {
                        if (compareDate(startTime, it)) {
                            endTime = it
                        } else {
                            dialogConfirm("结束时间不能小于开始时间")
                        }
                    }
                })
        }
    }
}


@Composable
fun ScheduleDateCard(
    startTime: String,
    endTime: String,
    startTimeClick: () -> Unit,
    endTimeClick: () -> Unit = {},
    selectPlan: () -> Unit = {}
) {
    var checked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = "日程设置",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            RoundCard {
                TaskItem("执行时间", startTime) { startTimeClick() }
                SpaceLine()
                TaskItem("结束时间", endTime) { endTimeClick() }
                SpaceLine()
                TaskItem("关联计划", content = {
                    SwitchButton(
                        modifier = Modifier.height(25.dp),
                        checked = checked,
                        onCheckedChange = { checked = it })
                })
            }
            AnimatedVisibility(visible = checked) {
                Column {
                    RoundCard {
                        TaskItem("是否重复", content = {
                            SwitchButton(
                                modifier = Modifier.height(25.dp),
                                checked = checked,
                                onCheckedChange = { checked = it })
                        })
                    }
                    RoundCard {
                        TaskItem("选择计划...", content = { }, onItemClick = { selectPlan() })
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CreateSchedulePreview() {
    CreateSchedulePage(navCtrl = rememberNavController())
}