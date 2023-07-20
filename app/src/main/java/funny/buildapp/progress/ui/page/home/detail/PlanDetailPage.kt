package funny.buildapp.progress.ui.page.home.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import funny.buildapp.progress.data.PlanRepository
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.home.newPlan.NewPlanPage
import funny.buildapp.progress.ui.page.home.newPlan.NewPlanViewModel
import funny.buildapp.progress.ui.page.home.plan.ProgressCard
import funny.buildapp.progress.ui.page.route.RouteUtils.back
import funny.buildapp.progress.ui.page.todo.TodoItem
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.black
import funny.buildapp.progress.ui.theme.cyan
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.daysBetweenDates
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.CustomBottomSheet


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlanDetailPage(
    navCtrl: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    viewModel: PlanDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val id = navBackStackEntry?.arguments?.getInt("id") ?: 0
    val plan = uiState.plan
    val todos = uiState.todos
    var bottomSheet by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.dispatch(PlanDetailAction.GetPlanDetail(id))
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.copy(0.2f))
    ) {
        Column {
            AppToolsBar(title = "计划详情",
                backgroundColor = transparent,
                tint = AppTheme.colors.themeUi,
                rightText = "编辑",
                onBack = { navCtrl.back() },
                onRightClick = { bottomSheet = !bottomSheet }
            )
//            完全版四级考纲词汇（乱序）
            val percentage = plan.initialValue.toDouble() / plan.targetValue.toDouble() * 100
            DetailContent(
                title = plan.title,
                startTime = plan.startDate.dateToString(),
                endTime = plan.endDate.dateToString(),
                progress = String.format("%.1f", percentage).toDouble(),
                proportion = "${plan.initialValue}/${plan.targetValue}",
                surplus = "${daysBetweenDates(getCurrentDate(), plan.endDate.dateToString())}",
                delay = "0"
            )
            Schedule(todos)
        }
        CustomBottomSheet(
            modifier = Modifier
                .background(black.copy(0.4f))
                .align(Alignment.BottomCenter),
            visible = bottomSheet,
            content = {
                NewPlanPage(
                    navCtrl = navCtrl,
                    isEditMode = true,
                    onDismiss = { bottomSheet = !bottomSheet },
                    id = id,
                )
            })
    }
}

@Composable
fun DetailContent(
    title: String,
    startTime: String,
    endTime: String,
    progress: Double,
    proportion: String,
    surplus: String,
    delay: String
) {
    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        item {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
            )
        }
        item { Spacer(modifier = Modifier.padding(vertical = 12.dp)) }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "从 $startTime  ",
                    color = AppTheme.colors.textSecondary,
                )
                Text(
                    text = "到 $endTime",
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
        item {
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = AppTheme.colors.error)) {
                    append(surplus)
                }
                append("天后结束  ")
                if (delay != "0") {
                    append("已延期")
                    withStyle(style = SpanStyle(color = AppTheme.colors.error)) {
                        append(delay)
                    }
                    append("天")
                }
            }
            Text(
                text = text,
                color = AppTheme.colors.textSecondary,
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "完成$progress%")
                    Text(text = proportion, color = Color.Gray)
                }
                Spacer(modifier = Modifier.padding(2.dp))
                LinearProgressIndicator(
                    progress = 20f / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = themeColor,
                    trackColor = themeColor.copy(0.2f)
                )
            }
        }
        item { Spacer(modifier = Modifier.padding(vertical = 10.dp)) }
        item {
            Text(
                text = "关联日程",
            )
        }

    }
}

@Composable
fun Schedule(todos: List<Todo>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(
            items = todos,
            key = { it.id },
            itemContent = {
                TodoItem(
                    selected = false,
                    title = "完全版四级考纲词汇（乱序）",
                )
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditPlanSheet(onDismiss: () -> Unit = {}, onItemClick: () -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
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
                    title = "编辑计划",
                    backgroundColor = cyan,
                    imageVector = Icons.Default.Close,
                    onRightClick = { onDismiss() },
                )
            }
            items(10) {
                ProgressCard(
                    progress = 27.7,
                    title = "完全版四级考纲词汇（乱序）",
                    status = "",
                    proportion = "1708/6145",
                    onClick = { onItemClick() }
                )
            }
        })
}