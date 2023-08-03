package funny.buildapp.progress.ui.route

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import funny.buildapp.progress.ui.page.home.detail.PlanDetailPage
import funny.buildapp.progress.ui.page.home.plan.PlanPage
import funny.buildapp.progress.ui.page.home.newPlan.NewPlanPage
import funny.buildapp.progress.ui.page.todo.create.CreateTodoPage
import funny.buildapp.progress.ui.page.todo.TodoPage
import funny.buildapp.progress.ui.page.daily.DailyPage

@RequiresApi(Build.VERSION_CODES.O)
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
        composable(
            route = Route.NEW_PLAN + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            NewPlanPage(navCtrl = navCtrl, id = id)
        }
        //task
        composable(route = Route.TODO) {
            DailyPage()
        }
        //detail
        composable(
            route = Route.PLAN_DETAIL + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            PlanDetailPage(navCtrl, it)
        }
        //schedule
        composable(route = Route.SCHEDULE) {
            TodoPage(navCtrl)
        }
        //schedule
        composable(route = Route.SCHEDULE) {
            TodoPage(navCtrl)
        }
        //create schedule
        composable(
            route = Route.CREATE_TODO + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            CreateTodoPage(navCtrl, it)
        }
    }
}

object Route {
    const val HOME = "home"
    const val NEW_PLAN = "newPlan"
    const val TODO = "todo"
    const val PLAN_DETAIL = "planDetail"
    const val SCHEDULE = "schedule"
    const val CREATE_TODO = "createTodo"
}
