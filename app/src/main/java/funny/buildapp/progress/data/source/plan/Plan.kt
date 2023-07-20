package funny.buildapp.progress.data.source.plan

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val initialValue: Int = 0,
    val targetValue: Int = 0,
    val status: Int = 0,
)
