package funny.buildapp.progress.data.source.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0,
    val isAssociatePlan: Boolean = false,
    val repeatable: Boolean = false,
    val associateId: Int = 0,
    val status: Int = 0,
)
