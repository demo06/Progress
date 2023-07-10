package funny.buildapp.progress.ui.page.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Today
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavRoute(
    var routeName: String,
    var title: String,
    var normalIcon: ImageVector,
    var pressIcon: ImageVector
) {
    object Home : BottomNavRoute(
        routeName = Route.HOME,
        title = "进度",
        normalIcon = Icons.Rounded.Home,
        pressIcon = Icons.Rounded.Home
    )

    object Task : BottomNavRoute(
        routeName = Route.TODO,
        title = "待办",
        normalIcon = Icons.Rounded.TaskAlt,
        pressIcon = Icons.Rounded.TaskAlt
    )
    object Schedule : BottomNavRoute(
        routeName = Route.SCHEDULE,
        title = "日程",
        normalIcon = Icons.Rounded.Today,
        pressIcon = Icons.Rounded.Today
    )
}