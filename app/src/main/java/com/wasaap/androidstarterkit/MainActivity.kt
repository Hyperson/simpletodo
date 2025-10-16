package com.wasaap.androidstarterkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasaap.androidstarterkit.ui.TodoApp
import com.wasaap.androidstarterkit.ui.rememberTodoAppState
import com.wasaap.core.androidstarterkit.core.designsystem.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = MainActivityUiState.Loading)
            val systemDark = isSystemInDarkTheme()

            val darkTheme = uiState.shouldUseDarkTheme(systemDark)

            TodoTheme(darkTheme = darkTheme) {
                TodoApp(appState = rememberTodoAppState())
            }
        }
    }
}