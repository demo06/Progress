package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.daily.Daily
import funny.buildapp.progress.data.source.daily.DailyDao
import funny.buildapp.progress.data.source.relation.DailyWithTodo
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.data.source.todo.TodoDao
import funny.buildapp.progress.utils.getCurrentDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoDao: TodoDao, private val dailyDao: DailyDao) {
    suspend fun getAll() = todoDao.getAll()
    suspend fun loadAllByIds(todoIds: IntArray) = todoDao.loadAllByIds(todoIds)
    suspend fun getTodoByPlanId(planId: Int) = todoDao.getTodoByPlanId(planId)
    suspend fun getTodoByDate(date: Long) = todoDao.getTodoByDate(date)
    suspend fun getTodoById(todoId: Long) = todoDao.getTodoById(todoId)
    suspend fun upsert(todo: Todo): Long {
        // TODO:  
        return todoDao.insertTodo(todo)
    }

    suspend fun upsertDaily(daily: Daily): Long = dailyDao.upsertTodo(daily)

    suspend fun delete(id: Long): Int = todoDao.delete(Todo(id = id))


    suspend fun getTodoFormDaily(): List<DailyWithTodo> {
        val daily = dailyDao.getDailyByDate(getCurrentDate(), getCurrentDate(false))
        val todo = todoDao.getTodoByDate(System.currentTimeMillis())
        if (daily.isEmpty()) {
            todo.map {
                upsertDaily(Daily(todoId = it.id, state = false))
            }
        } else {
            todo.forEach { todoTemp ->
                val dailyTemp = daily.find { it.todoId == todoTemp.id }
                if (dailyTemp == null) {
                    upsertDaily(Daily(todoId = todoTemp.id, state = false))
                }
            }
        }
        return getDailyWithTodo()
    }

    private suspend fun getDailyWithTodo(): List<DailyWithTodo> {
        return dailyDao.getDailyWithTodo(getCurrentDate(), getCurrentDate(false))
    }

}

