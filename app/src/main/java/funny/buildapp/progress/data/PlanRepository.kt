package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.plan.PlanDao
import javax.inject.Inject

class PlanRepository @Inject constructor(private val planDao: PlanDao) {
    suspend fun getAll(): List<Plan> = planDao.getAll()
    suspend fun findPlanById(planId: Int) = planDao.findPlanById(planId)

    suspend fun upsert(plan: Plan): Int = planDao.upsertPlan(plan)

    suspend fun delete(id: Int): Int = planDao.delete(Plan(id = id.toLong()))
}