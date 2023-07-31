package funny.buildapp.progress.data.source.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val startDate: Long = 0,
    val endDate: Long = 0,
    val initialValue: Int = 0,
    val targetValue: Int = 0,
    val status: Int = 0,
)
