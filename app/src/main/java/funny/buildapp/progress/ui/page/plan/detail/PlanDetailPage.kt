package funny.buildapp.progress.ui.page.plan.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.todo.TodoItem
import funny.buildapp.progress.ui.page.plan.newPlan.NewPlanPage
import funny.buildapp.progress.ui.route.Route
import funny.buildapp.progress.ui.route.RouteUtils
import funny.buildapp.progress.ui.route.RouteUtils.back
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.black
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.ui.theme.white
import funny.buildapp.progress.utils.dateToString
import funny.buildapp.progress.utils.daysBetweenDates
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.CustomBottomSheet
import kotlin.math.abs


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
            val percentage = plan.initialValue.toDouble() / plan.targetValue.toDouble() * 100
            val progress = if (percentage.isNaN()) 0.00 else String.format("%.1f", percentage).toDouble()
            val lastDay = daysBetweenDates(getCurrentDate().dateToString(), plan.endDate.dateToString())
            DetailContent(
                title = plan.title,
                startTime = plan.startDate.dateToString(),
                endTime = plan.endDate.dateToString(),
                progress = progress,
                proportion = "${plan.initialValue}/${plan.targetValue}",
                surplus = "${
                    daysBetweenDates(
                        getCurrentDate().dateToString(),
                        plan.endDate.dateToString()
                    )
                }",
                delay = lastDay
            )
            Schedule(todos, noDataClick = {
                RouteUtils.navTo(navCtrl, Route.CREATE_TODO, 0)
            })
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
    progress: Double = 0.00,
    proportion: String,
    surplus: String,
    delay: Long
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
                if (delay < 0) {
                    append("已延期")
                    withStyle(style = SpanStyle(color = AppTheme.colors.error)) {
                        append(abs(delay).toString())
                    }
                    append("天")
                } else {
                    withStyle(style = SpanStyle(color = AppTheme.colors.error)) {
                        append(surplus)
                    }
                    append("天后结束  ")
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
                    progress = (progress / 100).toFloat(),
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
fun Schedule(todos: List<Todo>, noDataClick: (() -> Unit?)? = null) {
    if (todos.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = todos,
                key = { it.id },
                itemContent = {
                    TodoItem(
                        selected = it.status == 1,
                        title = it.title,
                        showIcon = false,
                        isRepeatable = it.repeatable,
                    )
                }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                .background(white, shape = RoundedCornerShape(8.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "赶快去添加日程吧！~",
                modifier = Modifier
                    .clickable { noDataClick?.invoke() }
                    .background(themeColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                color = white,
            )
        }
    }
}
