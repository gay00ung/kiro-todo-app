package com.example.androidtodoapp.feature.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtodoapp.core.data.repository.TodoRepository
import com.example.androidtodoapp.core.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for creating and editing todos.
 */
@HiltViewModel
class TodoEditViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()
    
    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note.asStateFlow()
    
    private val _dueDate = MutableStateFlow<Date?>(null)
    val dueDate: StateFlow<Date?> = _dueDate.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var currentTodoId: String? = null
    
    /**
     * Initialize for creating a new todo.
     */
    fun initializeForNewTodo() {
        currentTodoId = null
        _title.value = ""
        _note.value = ""
        _dueDate.value = null
        _isLoading.value = false
    }
    
    /**
     * Load todo for editing.
     */
    fun loadTodo(todoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val todo = todoRepository.getTodoById(todoId)
                if (todo != null) {
                    currentTodoId = todo.id
                    _title.value = todo.title
                    _note.value = todo.note
                    _dueDate.value = todo.dueDate
                } else {
                    // Todo not found, initialize for new todo
                    initializeForNewTodo()
                }
            } catch (e: Exception) {
                // Handle error, initialize for new todo
                initializeForNewTodo()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Update title.
     */
    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }
    
    /**
     * Update note.
     */
    fun updateNote(newNote: String) {
        _note.value = newNote
    }
    
    /**
     * Update due date.
     */
    fun updateDueDate(newDueDate: Date?) {
        _dueDate.value = newDueDate
    }
    
    /**
     * Save todo.
     */
    fun saveTodo(onSaved: () -> Unit) {
        if (_title.value.isBlank()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val todo = Todo(
                    id = currentTodoId ?: UUID.randomUUID().toString(),
                    title = _title.value.trim(),
                    note = _note.value.trim(),
                    isDone = false,
                    dueDate = _dueDate.value,
                    createdAt = Date(),
                    updatedAt = Date()
                )
                todoRepository.saveTodo(todo)
                onSaved()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}