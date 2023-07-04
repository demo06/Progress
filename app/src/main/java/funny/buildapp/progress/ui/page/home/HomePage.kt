package funny.buildapp.progress.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.placeholder
import funny.buildapp.progress.ui.theme.green

@Composable
fun HomePage(navCtrl: NavHostController) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(green.copy(0.2f), Color.White)))
    ) {
        items(10) {
            ProgressCard(
                progress = 27.7f,
                title = "完全版四级考纲词汇（乱序）",
                status = "",
                proportion = "1708/6145"
            )
        }
    }
}

@Composable
fun ProgressCard(
    progress: Float = 0.0f,
    title: String = "",
    status: String = "",
    proportion: String = "0/0"
) {
    val isShowPlaceHolder by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .placeholder(isShowPlaceHolder),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = status, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Icon(Icons.Rounded.ArrowRight, contentDescription = "")
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .placeholder(isShowPlaceHolder),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "已学$progress%", fontSize = 12.sp)
            Text(text = proportion, fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.padding(2.dp))
        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .placeholder(isShowPlaceHolder),
            color = green,
            trackColor = green.copy(0.2f)
        )
    }
}