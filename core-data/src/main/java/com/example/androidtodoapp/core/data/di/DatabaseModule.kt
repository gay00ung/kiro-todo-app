package com.example.androidtodoapp.core.data.di

import android.content.Context
import androidx.room.Room
import com.example.androidtodoapp.core.data.TodoDao
import com.example.androidtodoapp.core.data.TodoDatabase
import com.example.androidtodoapp.core.data.repository.TodoRepository
import com.example.androidtodoapp.core.data.repository.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    
    @Binds
    abstract fun bindTodoRepository(
        todoRepositoryImpl: TodoRepositoryImpl
    ): TodoRepository
    
    companion object {
        
        @Provides
        @Singleton
        fun provideTodoDatabase(
            @ApplicationContext context: Context
        ): TodoDatabase {
            return Room.databaseBuilder(
                context,
                TodoDatabase::class.java,
                TodoDatabase.DATABASE_NAME
            ).build()
        }
        
        @Provides
        fun provideTodoDao(database: TodoDatabase): TodoDao {
            return database.todoDao()
        }
    }
}