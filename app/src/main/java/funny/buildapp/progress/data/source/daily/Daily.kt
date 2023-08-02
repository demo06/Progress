package funny.buildapp.progress.data.source.daily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
data class Daily(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val todoId: Long = 0,
    val changeDate: Long = System.currentTimeMillis(),
    val state: Boolean = false
)
