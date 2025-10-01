package com.wasaap.androidstarterkit.common.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val todoDispatcher: TodoDispatchers)

enum class TodoDispatchers {
    Default,
    IO,
}
