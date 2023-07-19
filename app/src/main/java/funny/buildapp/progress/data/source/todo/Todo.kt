package funny.buildapp.progress.data.source.todo

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import funny.buildapp.progress.data.source.DateConverter
import funny.buildapp.progress.data.source.plan.Plan
import java.util.Date

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = false,
    val associateId: Int = 0,
    val status: Int = 0,
)
