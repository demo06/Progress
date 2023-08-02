package funny.buildapp.progress.data.source.relation

import androidx.room.Embedded
import androidx.room.Relation
import funny.buildapp.progress.data.source.daily.Daily
import funny.buildapp.progress.data.source.todo.Todo

data class DailyWithTodo(
    @Embedded
    val daily: Daily,
    @Relation(
        parentColumn = "todoId",
        entityColumn = "id"
    )
    val todo: Todo
)
