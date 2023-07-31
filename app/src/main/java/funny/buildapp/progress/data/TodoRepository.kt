package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.data.source.todo.TodoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    suspend fun getAll() = todoDao.getAll()
    suspend fun loadAllByIds(todoIds: IntArray) = todoDao.loadAllByIds(todoIds)
    suspend fun getTodoByPlanId(planId: Int) = todoDao.getTodoByPlanId(planId)
    suspend fun getTodoByDate(date: Long) = todoDao.getTodoByDate(date)
    suspend fun getTodoById(todoId: Long) = todoDao.getTodoById(todoId)
    suspend fun upsert(todo: Todo): Long {
        return todoDao.insertTodo(todo)
    }

    suspend fun delete(id: Long): Int = todoDao.delete(Todo(id = id))
}

