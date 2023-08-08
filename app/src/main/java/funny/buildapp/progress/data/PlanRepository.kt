package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.plan.PlanDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository @Inject constructor(private val planDao: PlanDao) {
    suspend fun getAll(): List<Plan> = planDao.getAll()
    suspend fun findPlanById(planId: Int) = planDao.loadAllById(planId)
    suspend fun getPlanDetail(planId: Long): Plan = planDao.getPlanDetail(planId)
    suspend fun upsert(plan: Plan): Long = planDao.upsertPlan(plan)
    suspend fun delete(id: Long): Int = planDao.delete(Plan(id = id))
}