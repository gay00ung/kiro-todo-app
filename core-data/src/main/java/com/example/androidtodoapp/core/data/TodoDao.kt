package com.example.androidtodoapp.core.data

import androidx.room.*
import com.example.androidtodoapp.core.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Todo entities.
 * Provides reactive queries using Flow for automatic UI updates.
 */
@Dao
interface TodoDao {
    
    /**
     * Get all todos as a Flow for reactive updates.
     */
    @Query("SELECT * FROM todos ORDER BY updatedAt DESC")
    fun getAllTodos(): Flow<List<Todo>>
    
    /**
     * Get todos filtered by completion status.
     */
    @Query("SELECT * FROM todos WHERE isDone = :isDone ORDER BY updatedAt DESC")
    fun getTodosByStatus(isDone: Boolean): Flow<List<Todo>>
    
    /**
     * Search todos by title and note content.
     */
    @Query("""
        SELECT * FROM todos 
        WHERE title LIKE '%' || :query || '%' 
        OR note LIKE '%' || :query || '%'
        ORDER BY updatedAt DESC
    """)
    fun searchTodos(query: String): Flow<List<Todo>>
    
    /**
     * Get a specific todo by ID.
     */
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: String): Todo?
    
    /**
     * Insert a new todo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)
    
    /**
     * Update an existing todo.
     */
    @Update
    suspend fun updateTodo(todo: Todo)
    
    /**
     * Delete a todo.
     */
    @Delete
    suspend fun deleteTodo(todo: Todo)
    
    /**
     * Delete a todo by ID.
     */
    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodoById(id: String)
    
    /**
     * Toggle todo completion status.
     */
    @Query("UPDATE todos SET isDone = NOT isDone, updatedAt = :updatedAt WHERE id = :id")
    suspend fun toggleTodoCompletion(id: String, updatedAt: Long = System.currentTimeMillis())
}