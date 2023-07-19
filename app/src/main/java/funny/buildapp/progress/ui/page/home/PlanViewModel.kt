package funny.buildapp.progress.ui.page.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import funny.buildapp.progress.data.TodoRepository
import funny.buildapp.progress.data.source.todo.Todo
import funny.buildapp.progress.utils.loge
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(private val todoRepo: TodoRepository) : ViewModel() {
    suspend fun insert() {
        val state = todoRepo.delete(Todo(id = 3))
        state.loge()
    }

}