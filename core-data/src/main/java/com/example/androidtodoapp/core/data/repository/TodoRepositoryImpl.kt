package com.example.androidtodoapp.core.data.repository

import com.example.androidtodoapp.core.data.TodoDao
import com.example.androidtodoapp.core.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TodoRepository using Room database.
 * Provides offline-first data access with immediate UI updates.
 */
@Singleton
class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {
    
    override fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }
    
    override fun getTodosByStatus(isDone: Boolean): Flow<List<Todo>> {
        return todoDao.getTodosByStatus(isDone)
    }
    
    override fun searchTodos(query: String): Flow<List<Todo>> {
        return if (query.isBlank()) {
            todoDao.getAllTodos()
        } else {
            todoDao.searchTodos(query)
        }
    }
    
    override suspend fun getTodoById(id: String): Todo? {
        return todoDao.getTodoById(id)
    }
    
    override suspend fun saveTodo(todo: Todo) {
        val updatedTodo = todo.copy(updatedAt = java.util.Date())
        todoDao.insertTodo(updatedTodo)
    }
    
    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }
    
    override suspend fun deleteTodoById(id: String) {
        todoDao.deleteTodoById(id)
    }
    
    override suspend fun toggleTodoCompletion(id: String) {
        todoDao.toggleTodoCompletion(id)
    }
}