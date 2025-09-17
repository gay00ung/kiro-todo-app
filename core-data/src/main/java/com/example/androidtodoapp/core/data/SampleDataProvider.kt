package com.example.androidtodoapp.core.data

import com.example.androidtodoapp.core.data.repository.TodoRepository
import com.example.androidtodoapp.core.model.Todo
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides sample data for the app when no todos exist.
 */
@Singleton
class SampleDataProvider @Inject constructor(
    private val todoRepository: TodoRepository
) {
    
    suspend fun provideSampleDataIfNeeded() {
        val existingTodos = todoRepository.getAllTodos().first()
        
        if (existingTodos.isEmpty()) {
            val sampleTodos = listOf(
                Todo(
                    id = UUID.randomUUID().toString(),
                    title = "Welcome to Todo App!",
                    note = "This is a sample todo. Tap to edit or swipe to complete/delete.",
                    isDone = false,
                    dueDate = null,
                    createdAt = Date(),
                    updatedAt = Date()
                ),
                Todo(
                    id = UUID.randomUUID().toString(),
                    title = "Buy groceries",
                    note = "Milk, bread, eggs, and fruits",
                    isDone = false,
                    dueDate = Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), // Tomorrow
                    createdAt = Date(),
                    updatedAt = Date()
                ),
                Todo(
                    id = UUID.randomUUID().toString(),
                    title = "Completed task example",
                    note = "This shows how completed tasks look",
                    isDone = true,
                    dueDate = null,
                    createdAt = Date(),
                    updatedAt = Date()
                )
            )
            
            sampleTodos.forEach { todo ->
                todoRepository.saveTodo(todo)
            }
        }
    }
}