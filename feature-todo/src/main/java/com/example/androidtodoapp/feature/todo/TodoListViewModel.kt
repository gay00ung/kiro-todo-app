package com.example.androidtodoapp.feature.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtodoapp.core.data.repository.TodoRepository
import com.example.androidtodoapp.core.model.Todo
import com.example.androidtodoapp.core.model.TodoFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Todo list screen.
 * Manages UI state, search, filtering, and todo operations.
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val sampleDataProvider: com.example.androidtodoapp.core.data.SampleDataProvider
) : ViewModel() {
    
    init {
        // Provide sample data if needed
        viewModelScope.launch {
            sampleDataProvider.provideSampleDataIfNeeded()
        }
    }
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedFilter = MutableStateFlow(TodoFilter.ALL)
    val selectedFilter: StateFlow<TodoFilter> = _selectedFilter.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    /**
     * Combined flow of todos based on search query and filter.
     */
    val todos: StateFlow<List<Todo>> = combine(
        _searchQuery,
        _selectedFilter
    ) { query, filter ->
        Pair(query, filter)
    }.flatMapLatest { (query, filter) ->
        when {
            query.isNotBlank() -> {
                // Search takes precedence over filter
                todoRepository.searchTodos(query)
            }
            filter == TodoFilter.ACTIVE -> {
                todoRepository.getTodosByStatus(isDone = false)
            }
            filter == TodoFilter.DONE -> {
                todoRepository.getTodosByStatus(isDone = true)
            }
            else -> {
                todoRepository.getAllTodos()
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    /**
     * Update search query.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Update selected filter.
     */
    fun updateFilter(filter: TodoFilter) {
        _selectedFilter.value = filter
    }
    
    /**
     * Toggle todo completion status.
     */
    fun toggleTodoCompletion(todoId: String) {
        viewModelScope.launch {
            try {
                todoRepository.toggleTodoCompletion(todoId)
            } catch (e: Exception) {
                // Handle error - could emit to error state
            }
        }
    }
    
    /**
     * Delete a todo.
     */
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                todoRepository.deleteTodo(todo)
            } catch (e: Exception) {
                // Handle error - could emit to error state
            }
        }
    }
    
    /**
     * Delete a todo by ID.
     */
    fun deleteTodoById(todoId: String) {
        viewModelScope.launch {
            try {
                todoRepository.deleteTodoById(todoId)
            } catch (e: Exception) {
                // Handle error - could emit to error state
            }
        }
    }
}