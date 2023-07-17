package funny.buildapp.progress.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import funny.buildapp.progress.ui.page.route.AppNav
import funny.buildapp.progress.ui.page.route.Route
import funny.buildapp.progress.ui.page.route.RouteUtils
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.widgets.BottomBar


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AppScaffold() {
    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        floatingActionButton = {
            if (currentDestination?.route == Route.HOME || currentDestination?.route == Route.SCHEDULE) {
                FloatingActionButton(
                    containerColor = themeColor,
                    onClick = {
                        if (currentDestination.route == Route.HOME) {
                            RouteUtils.navTo(navCtrl, Route.NEW_TASK)
                        } else if (currentDestination.route == Route.SCHEDULE) {
                            RouteUtils.navTo(navCtrl, Route.CREATE_SCHEDULE, 0)
                        }

                    }
                ) {
                    Icon(
                        if (currentDestination.route == Route.HOME) {
                            Icons.Filled.Edit
                        } else {
                            Icons.Filled.AddTask
                        },
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
            when (currentDestination?.route) {
                Route.HOME -> BottomBar(navCtrl = navCtrl)
                Route.TODO -> BottomBar(navCtrl = navCtrl)
                Route.SCHEDULE -> BottomBar(navCtrl = navCtrl)
                else -> {}
            }
        },//nothing to do
        content = {
            AppNav(navCtrl = navCtrl, padding = it)
        },
    )
}