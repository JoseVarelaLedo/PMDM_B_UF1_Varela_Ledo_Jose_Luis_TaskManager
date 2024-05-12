package jose.uf1.taskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedTask = MutableLiveData<Task>()
    val selectedTask: LiveData<Task> get() = _selectedTask
    private val _selectedNote = MutableLiveData<Note>()
    val selectedNote: LiveData<Note> get () = _selectedNote

    fun selectTask(task: Task) {
        _selectedTask.value = task
    }

    fun selectNote (note: Note){
        _selectedNote.value = note
    }
}
