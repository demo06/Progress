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
    suspend fun getTodoById(planId: Int) = todoDao.getTodoById(planId)
    suspend fun upsert(todo: Todo): Long = todoDao.insertTodo(todo)
    suspend fun delete(id: Long): Int = todoDao.delete(Todo(id = id))
}