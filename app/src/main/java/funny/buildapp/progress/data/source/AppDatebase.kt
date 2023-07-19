package funny.buildapp.progress.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import funny.buildapp.progress.data.source.plan.Plan
import funny.buildapp.progress.data.source.plan.PlanDao
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.data.source.todo.TodoDao

@Database(entities = [Plan::class, Todo::class], version = 1) // 定义当前db的版本以及数据库表（数组可定义多张表）
@TypeConverters(DateConverter::class) // 定义类型转换器
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun planDao(): PlanDao
}
