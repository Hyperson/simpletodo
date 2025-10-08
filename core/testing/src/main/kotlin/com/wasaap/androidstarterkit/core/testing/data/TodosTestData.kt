package com.wasaap.androidstarterkit.core.testing.data

import com.wasaap.androidstarterkit.core.model.Todo

val todosTestData: List<Todo> = listOf(
    Todo(id = "1", name = "Write unit tests", done = false),
    Todo(id = "2", name = "Fix bug #42", done = true),
    Todo(id = "3", name = "Review PR for feature X", done = false),
)