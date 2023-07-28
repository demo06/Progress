package funny.buildapp.progress.data

import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.plan.PlanDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository @Inject constructor(private val planDao: PlanDao) {
    suspend fun getAll(): List<Plan> = planDao.getAll()
    suspend fun findPlanById(planIds: IntArray) = planDao.loadAllById(planIds)
    suspend fun getPlanDetail(planId: Int): Plan = planDao.getPlanDetail(planId)
    suspend fun upsert(plan: Plan): Long = planDao.insertPlan(plan)
    suspend fun delete(id: Long): Int = planDao.delete(Plan(id = id))
}