package funny.buildapp.progress.data.source.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import funny.buildapp.progress.utils.getCurrentDate

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val startDate: Long = getCurrentDate(),
    val endDate: Long = getCurrentDate(false),
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = false,
    val associateId: Int = 0,
    val status: Int = 0,
)
