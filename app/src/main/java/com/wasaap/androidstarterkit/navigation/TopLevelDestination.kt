package com.wasaap.androidstarterkit.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.wasaap.androidstarterkit.feature.mytodos.navigation.TodosBaseRoute
import com.wasaap.androidstarterkit.feature.mytodos.navigation.TodosRoute
import com.wasaap.androidstarterkit.feature.settings.navigation.SettingsRoute
import com.wasaap.core.androidstarterkit.core.designsystem.icon.TodoIcons
import kotlin.reflect.KClass
import com.wasaap.androidstarterkit.feature.mytodos.R as myTodos
import com.wasaap.androidstarterkit.feature.settings.R as settings


enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @param:StringRes val iconTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    TODOS(
        selectedIcon = TodoIcons.ViewDay,
        unselectedIcon = TodoIcons.ViewDay,
        iconTextId = myTodos.string.feature_mytodos_title,
        route = TodosRoute::class,
        baseRoute = TodosBaseRoute::class,
    ),
    SETTINGS(
        selectedIcon = TodoIcons.Settings,
        unselectedIcon = TodoIcons.Settings,
        iconTextId = settings.string.feature_settings_title,
        route = SettingsRoute::class,
    )
}
