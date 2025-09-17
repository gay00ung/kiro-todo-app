package com.example.androidtodoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Todo App.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize app-level components here if needed
    }
}