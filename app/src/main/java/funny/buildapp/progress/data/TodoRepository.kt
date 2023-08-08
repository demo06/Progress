package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.daily.Daily
import funny.buildapp.progress.data.source.daily.DailyDao
import funny.buildapp.progress.data.source.plan.PlanDao
import funny.buildapp.progress.data.source.relation.DailyWithTodo
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.data.source.todo.TodoDao
import funny.buildapp.progress.utils.getCurrentDate
import funny.buildapp.progress.utils.loge
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val dailyDao: DailyDao,
    private val planDao: PlanDao
) {
    suspend fun getAll() = todoDao.getAll()
    suspend fun loadAllByIds(todoIds: IntArray) = todoDao.loadAllByIds(todoIds)
    suspend fun getTodoByPlanId(planId: Int): List<Todo> {
        return todoDao.getTodoByPlanId(planId)
    }

    suspend fun getTodoByDate(date: Long) = todoDao.getTodoByDate(date)
    suspend fun getTodoById(todoId: Long) = todoDao.getTodoById(todoId)
    suspend fun upsert(todo: Todo): Long {
        return todoDao.insertTodo(todo)
    }

    suspend fun upsertDaily(daily: Daily): Long {
        daily.loge()
        if (daily.planId != 0L) {
            val plan = planDao.getPlanDetail(daily.planId)
            if (daily.state) {
                planDao.upsertPlan(
                    plan.copy(
                        initialValue = plan.initialValue + 1,
                        status = if (plan.initialValue + 1 > 0) 1 else 2
                    )
                )
            } else {
                if (plan.initialValue > 0) {
                    planDao.upsertPlan(
                        plan.copy(
                            initialValue = plan.initialValue - 1,
                            status = if (plan.initialValue - 1 == 0) 0 else 1
                        )
                    )
                }
            }
        }
        return dailyDao.upsertTodo(daily)
    }

    suspend fun delete(id: Long): Int = todoDao.delete(Todo(id = id))


    suspend fun getTodoFormDaily(): List<DailyWithTodo> {
        val daily = dailyDao.getDailyByDate(getCurrentDate(), getCurrentDate(false))
        val todo = todoDao.getTodoByDate(System.currentTimeMillis())
        if (daily.isEmpty()) {
            todo.map {
                upsertDaily(Daily(todoId = it.id, state = false, planId = it.associateId))
            }
        } else {
            todo.forEach { todoTemp ->
                val dailyTemp = daily.find { it.todoId == todoTemp.id }
                if (dailyTemp == null) {
                    upsertDaily(Daily(todoId = todoTemp.id, state = false, planId = todoTemp.associateId))
                }
            }
        }
        return getDailyWithTodo()
    }

    private suspend fun getDailyWithTodo(): List<DailyWithTodo> {
        return dailyDao.getDailyWithTodo(getCurrentDate(), getCurrentDate(false))
    }

}

