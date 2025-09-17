package com.example.androidtodoapp.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Todo data model representing a single todo item.
 */
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: String,
    val title: String,
    val note: String = "",
    val isDone: Boolean = false,
    val dueDate: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)