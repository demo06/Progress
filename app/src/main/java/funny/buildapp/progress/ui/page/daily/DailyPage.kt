package funny.buildapp.progress.ui.page.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import funny.buildapp.progress.ui.page.home.plan.ScheduleToolBar
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.widgets.clickWithoutWave

@Composable
fun DailyPage(viewModel: DailyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val todos = uiState.todos
    LaunchedEffect(Unit) {
        viewModel.dispatch(DailyAction.Load)
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.themeUi.copy(0.2f))
    ) {
        item {
            ScheduleToolBar(title = "今日待办")
        }
        items(items = todos.filter { !it.daily.state }, key = { it.daily.id }) {
            TodoItem(title = it.todo.title, selected = it.daily.state,
                onClick = { viewModel.dispatch(DailyAction.UpTodayTask(it.daily)) }
            )
        }
        if (todos.any { it.daily.state }) {
            item {
                Label()
            }
        }
        items(items = todos.filter { it.daily.state }, key = { it.daily.id }) {
            TodoItem(title = it.todo.title, selected = it.daily.state,
                onClick = { viewModel.dispatch(DailyAction.UpTodayTask(it.daily)) }
            )
        }
    }
}


@Composable
fun TodoItem(
    title: String,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    backgroundColor: Color = Color.White,
    showIcon: Boolean = true,
    isRepeatable: Boolean = false,
) {
    Row(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .clickWithoutWave { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showIcon) {
                Icon(
                    imageVector = if (selected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = "icon",
                    tint = if (selected) AppTheme.colors.themeUi else Color.Gray
                )
            }
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = title,
                textDecoration = if (selected) TextDecoration.LineThrough else null,
                color = if (selected) Color.Gray else Color.Unspecified
            )
        }
        if (isRepeatable) {
            Icon(
                imageVector = Icons.Rounded.EventRepeat,
                contentDescription = "icon",
                tint = if (selected) AppTheme.colors.themeUi else Color.Gray
            )
        }
    }
}


@Composable
fun Label() {
    Column(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(AppTheme.colors.themeUi.copy(0.8f), RoundedCornerShape(6.dp))
            .clickWithoutWave { }
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = "已完成",
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textThird,
        )
    }
}
