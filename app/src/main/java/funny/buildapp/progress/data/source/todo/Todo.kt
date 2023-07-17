package funny.buildapp.progress.data.source.todo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import funny.buildapp.progress.data.source.plan.Plan

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: String = "0",
    val endDate: String = "0",
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = false,
    val associateId: Int = 0,
    val status: Int = 0,
)
