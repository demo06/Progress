package funny.buildapp.progress.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
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


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AppScaffold() {
    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        floatingActionButton = {
            if (currentDestination?.route == Route.HOME) {
                FloatingActionButton(
                    containerColor = themeColor,
                    onClick = { RouteUtils.navTo(navCtrl, Route.NEW_TASK) }
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = { },//nothing to do
        content = {
            AppNav(navCtrl = navCtrl, padding = it)
        },
    )
}