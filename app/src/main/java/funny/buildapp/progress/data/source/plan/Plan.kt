package funny.buildapp.progress.data.source.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: String = "0",
    val endDate: String = "0",
    val initialValue: Int = 0,
    val endValue: Int = 0,
    val status: Int = 0,
)
