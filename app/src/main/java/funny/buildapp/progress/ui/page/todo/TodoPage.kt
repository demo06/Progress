package funny.buildapp.progress.ui.page.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.page.home.ScheduleToolBar
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient2
import funny.buildapp.progress.widgets.clickWithoutWave

@Composable
fun TodoPage(navCtrl: NavHostController) {
    var selected by remember { mutableStateOf(false) }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(backgroundGradient2)
    ) {
        item {
            ScheduleToolBar(title = "今日待办")
        }
        items(10) {
            TodoItem(selected = selected, onClick = {
                selected = !selected
            })
        }
        item {
            Label()
        }
        items(10) {
            TodoItem(selected = true, onClick = {})
        }
    }
}


@Composable
fun TodoItem(selected: Boolean = false, onClick: () -> Unit) {
    Row(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(4.dp))
            .clickWithoutWave { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (selected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = "icon",
            tint = if (selected) AppTheme.colors.themeUi else Color.Gray
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "完全版四级考纲词汇（乱序）",
            textDecoration = if (selected) TextDecoration.LineThrough else null,
            color = if (selected) Color.Gray else Color.Unspecified
        )
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

@Preview(showBackground = true)
@Composable
fun TaskPagePreview() {
    TodoPage(navCtrl = rememberNavController())
}