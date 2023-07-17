package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.todo.TodoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

}