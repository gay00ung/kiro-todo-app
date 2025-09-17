package com.example.androidtodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.androidtodoapp.feature.todo.TodoEditScreen
import com.example.androidtodoapp.feature.todo.TodoListScreen
import com.example.androidtodoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoApp()
                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }
    
    when (val screen = currentScreen) {
        is Screen.List -> {
            TodoListScreen(
                onNavigateToEdit = { todoId ->
                    currentScreen = Screen.Edit(todoId)
                },
                onNavigateToSettings = {
                    // TODO: Implement settings screen
                }
            )
        }
        is Screen.Edit -> {
            TodoEditScreen(
                todoId = screen.todoId,
                onNavigateBack = {
                    currentScreen = Screen.List
                }
            )
        }
    }
}

sealed class Screen {
    object List : Screen()
    data class Edit(val todoId: String?) : Screen()
}