package funny.buildapp.progress.data.source.relation

import androidx.room.Embedded
import androidx.room.Relation
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.todo.Todo

data class TodoWithPlan(
    @Embedded
    val todo: Todo,
    @Relation(
        parentColumn = "associateId",
        entityColumn = "id"
    )
    val plan: Plan
)
