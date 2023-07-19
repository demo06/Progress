package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.data.source.todo.TodoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    suspend fun getAll() = todoDao.getAll()
    suspend fun loadAllByIds(todoIds: IntArray) = todoDao.loadAllByIds(todoIds)
    suspend fun upsert(todo: Todo): Int = todoDao.upsertTodo(todo)


    suspend fun delete(id: Int): Int = todoDao.delete(Todo(id = id.toLong()))
}