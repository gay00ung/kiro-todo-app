package com.example.androidtodoapp.core.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.androidtodoapp.core.model.Todo

/**
 * Room database for the Todo app.
 * Provides access to TodoDao and handles database configuration.
 */
@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    
    abstract fun todoDao(): TodoDao
    
    companion object {
        const val DATABASE_NAME = "todo_database"
    }
}

/**
 * Type converters for Room database.
 * Handles conversion between complex types and primitive types.
 */
class Converters {
    
    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }
    
    @androidx.room.TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}