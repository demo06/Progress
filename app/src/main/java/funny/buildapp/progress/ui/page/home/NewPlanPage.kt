package funny.buildapp.progress.ui.page.home

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import funny.buildapp.progress.widgets.clickWithoutWave
import kotlinx.coroutines.launch

@Composable
fun NewPlanPage(navCtrl: NavHostController) {
    var title by remember { mutableStateOf("") }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
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
                    title = "新建目标",
                    tint = AppTheme.colors.themeUi,
                    backgroundColor = transparent,
                    onBack = { navCtrl.back() },
                )
            }
            item {
                PlanTitle(
                    text = title,
                    onTextChange = {
                        title = it
                    })
            }
            item {
                DateCard(
                    startTime = startTime,
                    endTime = endTime,
                    startTimeClick = {
                        dialogState = 0
                        openDialog = !openDialog
                    },
                    endTimeClick = {
                        dialogState = 1
                        openDialog = !openDialog
                    })
            }
            item {
                ProportionCard()
            }
            item {
                FillWidthButton(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    text = "保存"
                ) { }
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
                            snackScope.launch {
                                snackState.showSnackbar(
                                    "开始时间不能大于结束时间"
                                )
                            }
                        }
                    } else {
                        if (compareDate(startTime, it)) {
                            endTime = it
                        } else {
                            snackScope.launch {
                                snackState.showSnackbar(
                                    "结束时间不能小于开始时间"
                                )
                            }
                        }
                    }
                })
        }
        SnackbarHost(hostState = snackState, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun PlanTitle(
    text: String = "",
    hint: String = "在这里输入目标标题",
    title: String = "目标名称",
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
                text = "目标时间",
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
fun ProportionCard() {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = "目标比例",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            RoundCard {
                TaskItem("起始量", "0")
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppTheme.colors.divider)
                )
                TaskItem("结束量", "100")
            }
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


@Preview(showBackground = true)
@Composable
fun NewTaskPreview() {
    NewPlanPage(navCtrl = rememberNavController())
}