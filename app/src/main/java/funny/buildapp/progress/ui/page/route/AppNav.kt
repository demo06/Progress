package funny.buildapp.progress.ui.page.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import funny.buildapp.progress.ui.page.detail.DetailPage
import funny.buildapp.progress.ui.page.home.PlanPage
import funny.buildapp.progress.ui.page.home.NewPlanPage
import funny.buildapp.progress.ui.page.schedule.CreateSchedulePage
import funny.buildapp.progress.ui.page.schedule.SchedulePage
import funny.buildapp.progress.ui.page.todo.TodoPage

@Composable
fun AppNav(navCtrl: NavHostController, padding: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navCtrl,
        startDestination = Route.HOME
    ) {
        //home
        composable(route = Route.HOME) {
            PlanPage(navCtrl)
        }
        //new task
        composable(route = Route.NEW_TASK) {
            NewPlanPage(navCtrl)
        }
        //task
        composable(route = Route.TODO) {
            TodoPage(navCtrl)
        }
        //detail
        composable(route = Route.DETAIL) {
            DetailPage(navCtrl)
        }
        //schedule
        composable(route = Route.SCHEDULE) {
            SchedulePage(navCtrl)
        }
        //schedule
        composable(route = Route.SCHEDULE) {
            SchedulePage(navCtrl)
        }
        //create schedule
        composable(route = Route.CREATE_SCHEDULE) {
            CreateSchedulePage(navCtrl)
        }
    }
}

object Route {
    const val HOME = "home"
    const val NEW_TASK = "newTask"
    const val TODO = "todo"
    const val DETAIL = "detail"
    const val SCHEDULE = "schedule"
    const val CREATE_SCHEDULE = "createSchedule"
}
