package funny.buildapp.progress.data.source.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todos WHERE id IN (:todoIds)")
    suspend fun loadAllByIds(todoIds: IntArray): List<Todo>

    @Upsert
    suspend fun upsertTodo(todos: Todo): Int

    @Delete
    suspend fun delete(todo: Todo): Int
}