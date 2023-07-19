package funny.buildapp.progress.data.source.plan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans")
    suspend fun getAll(): List<Plan>

    @Query("SELECT * FROM plans WHERE id IN (:planIds)")
    suspend fun loadAllById(planIds: IntArray): List<Plan>

    @Upsert
    suspend fun insertPlan(plans: Plan): Long

    @Delete
    suspend fun delete(plan: Plan): Int
}