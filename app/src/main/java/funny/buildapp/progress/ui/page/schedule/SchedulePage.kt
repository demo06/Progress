package funny.buildapp.progress.ui.page.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.white
import funny.buildapp.progress.widgets.RoundCard
import funny.buildapp.progress.widgets.clickWithoutWave

@Composable
fun SchedulePage(navCtrl: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(backgroundGradient),
    ) {
        DateCard()
        ScheduleCard()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCard() {
    val datePickerState = rememberDatePickerState()
    DatePicker(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.themeUi.copy(0.1f)),
        state = datePickerState,
        headline = null,
        title = null,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            currentYearContentColor = AppTheme.colors.themeUi,
            selectedYearContainerColor = AppTheme.colors.themeUi,
            selectedDayContainerColor = AppTheme.colors.themeUi,
            todayContentColor = AppTheme.colors.themeUi.copy(0.8f),
            todayDateBorderColor = AppTheme.colors.themeUi.copy(0.8f),
        )
    )
}


@Composable
fun ScheduleCard() {
    LazyColumn(
        modifier = Modifier
            .background(white)
            .fillMaxWidth(),
    ) {
        items(20) {
            ScheduleItem()
        }
    }
}

@Composable
fun ScheduleItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "完全版四级考纲词汇（乱序）",
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
    SchedulePage(navCtrl = rememberNavController())
}