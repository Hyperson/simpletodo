package com.wasaap.core.androidstarterkit.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wasaap.core.androidstarterkit.designsystem.icon.TodoIcons
import com.wasaap.core.androidstarterkit.designsystem.theme.TodoTheme

@Composable
fun TodoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    TodoTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        TodoButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun TodoTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        content = content,
    )
}

@Composable
private fun TodoButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier.padding(
                start = if (leadingIcon != null) {
                    ButtonDefaults.IconSpacing
                } else {
                    0.dp
                },
            ),
    ) {
        text()
    }
}


@ThemePreviews
@Composable
fun TodoTextButtonPreview() {
    TodoTheme {
        TodoBackground(modifier = Modifier.size(150.dp, 50.dp)) {
            TodoTextButton(
                onClick = {},
            ) {
                Text(text = "Text Button")
            }
        }
    }
}

@ThemePreviews
@Composable
fun TodoButtonWithIcon() {
    TodoTheme {
        TodoBackground(modifier = Modifier.size(150.dp, 50.dp)) {
            TodoTextButton(
                onClick = {},
                enabled = false,
                text = { Text(text = "Text Button") },
                leadingIcon = {
                    Icon(imageVector = TodoIcons.Add, contentDescription = null)
                },
            )
        }
    }
}
