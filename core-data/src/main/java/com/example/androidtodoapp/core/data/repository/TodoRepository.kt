package com.example.androidtodoapp.core.data.repository

import com.example.androidtodoapp.core.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Todo data operations.
 * Provides an abstraction layer between ViewModels and data sources.
 */
interface TodoRepository {
    
    /**
     * Get all todos as a Flow for reactive updates.
     */
    fun getAllTodos(): Flow<List<Todo>>
    
    /**
     * Get todos filtered by completion status.
     */
    fun getTodosByStatus(isDone: Boolean): Flow<List<Todo>>
    
    /**
     * Search todos by query string.
     */
    fun searchTodos(query: String): Flow<List<Todo>>
    
    /**
     * Get a specific todo by ID.
     */
    suspend fun getTodoById(id: String): Todo?
    
    /**
     * Insert or update a todo.
     */
    suspend fun saveTodo(todo: Todo)
    
    /**
     * Delete a todo.
     */
    suspend fun deleteTodo(todo: Todo)
    
    /**
     * Delete a todo by ID.
     */
    suspend fun deleteTodoById(id: String)
    
    /**
     * Toggle todo completion status.
     */
    suspend fun toggleTodoCompletion(id: String)
}