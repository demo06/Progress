package funny.buildapp.progress.ui.page.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import funny.buildapp.progress.ui.page.detail.DetailPage
import funny.buildapp.progress.ui.page.home.HomePage
import funny.buildapp.progress.ui.page.newtask.NewTaskPage

@Composable
fun AppNav(navCtrl: NavHostController, padding: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navCtrl,
        startDestination = Route.HOME
    ) {
        //home
        composable(route = Route.HOME) {
            HomePage(navCtrl)
        }
        //new task
        composable(route = Route.NEW_TASK) {
            NewTaskPage(navCtrl)
        }
        //detail
        composable(route = Route.DETAIL) {
            DetailPage(navCtrl)
        }
    }
}

object Route {
    const val HOME = "home"
    const val NEW_TASK = "newTask"
    const val DETAIL = "detail"
}
