package funny.buildapp.progress.ui.page.newtask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.page.route.RouteUtils.back
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.backgroundGradient
import funny.buildapp.progress.ui.theme.transparent
import funny.buildapp.progress.widgets.AppToolsBar
import funny.buildapp.progress.widgets.RoundCard

@Composable
fun NewTaskPage(navCtrl: NavHostController) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        item {
            AppToolsBar(
                title = "新建目标",
                tint = AppTheme.colors.themeUi,
                backgroundColor = transparent,
                onBack = { navCtrl.back() },
            )
        }
        item { HeadCard() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadCard() {
    var text by remember { mutableStateOf("") }
    Column {
        Text(
            text = "目标名称",
            fontWeight = FontWeight.Bold,

            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
        RoundCard {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxSize()
                    .background(transparent),
                placeholder = {
                    Text(
                        text = "请输入目标名称",
                        color = AppTheme.colors.placeholder
                    )
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun NewTaskPreview() {
    NewTaskPage(navCtrl = rememberNavController())
}