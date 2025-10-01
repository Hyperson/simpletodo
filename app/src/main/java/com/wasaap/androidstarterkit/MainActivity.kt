package com.wasaap.androidstarterkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import com.wasaap.androidstarterkit.ui.TodoApp
import com.wasaap.androidstarterkit.ui.rememberTodoAppState
import com.wasaap.core.androidstarterkit.designsystem.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberTodoAppState()
            val systemDark = isSystemInDarkTheme()

            TodoTheme(darkTheme = systemDark) {
                TodoApp(
                    appState = appState,
                )
            }
        }
    }
}