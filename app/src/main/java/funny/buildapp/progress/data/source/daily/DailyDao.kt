package funny.buildapp.progress.data.source.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import funny.buildapp.progress.data.source.relation.DailyWithTodo

@Dao
interface DailyDao {
    @Query("SELECT * FROM daily")
    suspend fun getAll(): List<Daily>

    @Query("SELECT * FROM daily WHERE id IN (:id)")
    suspend fun findById(id: Long): Daily

    @Query("SELECT * FROM daily WHERE changeDate BETWEEN :start AND :end")
    suspend fun getDailyByDate(start: Long, end: Long): List<Daily>

    @Transaction
    @Query("SELECT * FROM daily where changeDate BETWEEN :start AND :end AND todoId IN (SELECT id FROM todos )")
    suspend fun getDailyWithTodo(start: Long, end: Long):List<DailyWithTodo>

    @Upsert
    suspend fun upsertTodo(daily: Daily): Long

    @Delete
    suspend fun delete(daily: Daily): Int

}