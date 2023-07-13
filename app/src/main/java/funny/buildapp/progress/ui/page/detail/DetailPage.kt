package funny.buildapp.progress.ui.page.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.page.route.RouteUtils.back
import funny.buildapp.progress.ui.page.todo.TodoItem
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.backgroundGradient2
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.ui.theme.white
import funny.buildapp.progress.widgets.AppToolsBar

@Composable
fun DetailPage(navCtrl: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.copy(0.2f))
    ) {
        AppToolsBar(title = "计划详情",
            backgroundColor = transparent,
            tint = AppTheme.colors.themeUi,
            rightText = "编辑",
            onBack = { navCtrl.back() }
        )
        DetailContent(
            title = "完全版四级考纲词汇（乱序）",
            startTime = "2021-08-01",
            endTime = "2021-08-31",
            progress = 20.7f,
            proportion = "20/100",
            surplus = "100",
            delay = "1"
        )
        Schedule()
    }
}

@Composable
fun DetailContent(
    title: String,
    startTime: String,
    endTime: String,
    progress: Float,
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
                text = title, fontWeight = FontWeight.Bold,
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
                append("已延期")
                withStyle(style = SpanStyle(color = AppTheme.colors.error)) {
                    append(delay)
                }
                append("天")
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
fun Schedule() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(20) {
            TodoItem(
                selected = it < 10,
                title = "完全版四级考纲词汇（乱序）",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    DetailPage(navCtrl = rememberNavController())
}