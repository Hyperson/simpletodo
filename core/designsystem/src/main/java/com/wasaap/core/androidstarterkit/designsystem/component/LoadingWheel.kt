package com.wasaap.core.androidstarterkit.designsystem.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wasaap.core.androidstarterkit.designsystem.theme.TodoTheme

@Composable
fun TodoLoadingWheel(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
    )
}

@ThemePreviews
@Composable
fun TodoLoadingWheelPreview() {
    TodoTheme {
        Surface {
            TodoLoadingWheel()
        }
    }
}