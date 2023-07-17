package funny.buildapp.progress.data.source.plan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlanDao {
    @Query("SELECT * FROM plans")
    fun getAll(): List<Plan>

    @Query("SELECT * FROM plans WHERE id IN (:planIds)")
    fun loadAllByIds(planIds: IntArray): List<Plan>

    @Insert
    fun insertPlan(plans: Plan)

    @Delete
    fun delete(plan: Plan)
}