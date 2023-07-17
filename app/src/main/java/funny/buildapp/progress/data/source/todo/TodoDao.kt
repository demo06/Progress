package funny.buildapp.progress.data.source.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM todos WHERE id IN (:todoIds)")
    fun loadAllByIds(todoIds: IntArray): List<Todo>

    @Insert
    fun insertTodo(todos: Todo)

    @Delete
    fun delete(todo: Todo)
}