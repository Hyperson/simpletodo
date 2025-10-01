package com.wasaap.core.androidstarterkit.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wasaap.core.androidstarterkit.designsystem.icon.TodoIcons
import com.wasaap.core.androidstarterkit.designsystem.theme.TodoTheme

@Composable
fun RowScope.TodoNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TodoNavigationDefaults.navigationContentColor(),
            selectedTextColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TodoNavigationDefaults.navigationContentColor(),
            indicatorColor = TodoNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun TodoNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = TodoNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}

@Composable
fun TodoNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TodoNavigationDefaults.navigationContentColor(),
            selectedTextColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TodoNavigationDefaults.navigationContentColor(),
            indicatorColor = TodoNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun TodoNavigationRail(
    modifier: Modifier = Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = TodoNavigationDefaults.navigationContentColor(),
        header = header,
        content = content,
    )
}

@Composable
fun TodoNavigationSuiteScaffold(
    navigationSuiteItems: TodoNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TodoNavigationDefaults.navigationContentColor(),
            selectedTextColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TodoNavigationDefaults.navigationContentColor(),
            indicatorColor = TodoNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TodoNavigationDefaults.navigationContentColor(),
            selectedTextColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TodoNavigationDefaults.navigationContentColor(),
            indicatorColor = TodoNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = TodoNavigationDefaults.navigationContentColor(),
            selectedTextColor = TodoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = TodoNavigationDefaults.navigationContentColor(),
        ),
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TodoNavigationSuiteScope(
                navigationSuiteScope = this,
                navigationSuiteItemColors = navigationSuiteItemColors,
            ).run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContentColor = TodoNavigationDefaults.navigationContentColor(),
            navigationRailContainerColor = Color.Transparent,
        ),
        modifier = modifier,
    ) {
        content()
    }
}

class TodoNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors,
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        colors = navigationSuiteItemColors,
        modifier = modifier,
    )
}

@ThemePreviews
@Composable
fun TodoNavigationBarPreview() {
    val items = listOf("Todos", "Settings")
    //TODO
    val icons = listOf(
        TodoIcons.ViewDay,
        TodoIcons.Settings,
    )
    val selectedIcons = listOf(
        TodoIcons.ViewDay,
        TodoIcons.Settings,
    )


    TodoTheme {
        TodoNavigationBar {
            items.forEachIndexed { index, item ->
                TodoNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = selectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun TodoNavigationRailPreview() {
    val items = listOf("Todos", "Settings")
    //TODO
    val icons = listOf(
        TodoIcons.ViewDay,
        TodoIcons.Settings,
    )
    val selectedIcons = listOf(
        TodoIcons.ViewDay,
        TodoIcons.Settings,
    )

    TodoTheme {
        TodoNavigationRail {
            items.forEachIndexed { index, item ->
                TodoNavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = selectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}

object TodoNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}
