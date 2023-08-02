package funny.buildapp.progress.ui.page.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.ui.page.route.Route
import funny.buildapp.progress.ui.page.route.RouteUtils
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPage(navCtrl: NavHostController, viewModel: TodoViewModel = hiltViewModel()) {
    val datePickerState = rememberDatePickerState()
    val uiState by viewModel.uiState.collectAsState()
    val todos = uiState.todos
    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }.collect {
            viewModel.dispatch(TodoAction.GetTodoList(datePickerState.selectedDateMillis ?: 0))
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.copy(0.2f))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(white),
        ) {
            DatePane(datePickerState)
            ScheduleCard(todos = todos, onItemClick = {
                RouteUtils.navTo(
                    navCtrl = navCtrl,
                    destinationName = Route.CREATE_TODO,
                    args = it.toInt(),
                )
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePane(datePickerState: DatePickerState) {
    DatePicker(
        state = datePickerState,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.themeUi.copy(0.2f)),
        colors = DatePickerDefaults.colors(
            currentYearContentColor = AppTheme.colors.themeUi,
            selectedYearContainerColor = AppTheme.colors.themeUi,
            selectedDayContainerColor = AppTheme.colors.themeUi,
            todayContentColor = AppTheme.colors.themeUi.copy(0.8f),
            todayDateBorderColor = AppTheme.colors.themeUi.copy(0.8f),
        ), title = null, headline = null, showModeToggle = false
    )
}


@Composable
fun ScheduleCard(todos: List<Todo>, onItemClick: (Long) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(white)
            .fillMaxWidth(),
    ) {
        items(
            items = todos,
            key = { it.id },
            itemContent = {
                ScheduleItem(it.title) {
                    onItemClick(it.id)
                }
            }
        )
    }
}

@Composable
fun ScheduleItem(text: String, onItemClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(AppTheme.colors.divider)
                .fillMaxWidth()
                .height(1.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SchedulePreview() {
    TodoPage(navCtrl = rememberNavController())
}