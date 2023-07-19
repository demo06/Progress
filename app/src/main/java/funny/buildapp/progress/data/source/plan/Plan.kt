package funny.buildapp.progress.data.source.plan

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import funny.buildapp.progress.data.source.DateConverter
import java.util.Date

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val initialValue: Int = 0,
    val endValue: Int = 0,
    val status: Int = 0,
)
