package funny.buildapp.progress.data.source.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<Todo>

    @Query("SELECT * FROM todos WHERE id IN (:todoIds)")
    suspend fun loadAllByIds(todoIds: IntArray): List<Todo>

    @Query("SELECT * FROM todos WHERE id IN (:planId)")
    suspend fun getTodoByPlanId(planId: Int): List<Todo>

    @Query("SELECT * FROM todos WHERE id IN (:todoId)")
    suspend fun getTodoById(todoId: Int): Todo

    @Query("SELECT * FROM todos WHERE :date BETWEEN startDate and endDate")
    suspend fun getTodoByDate(date: Long): List<Todo>

    @Upsert
    suspend fun insertTodo(todo: Todo): Long

    @Delete
    suspend fun delete(todo: Todo): Int
}