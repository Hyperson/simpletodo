package com.wasaap.androidstarterkit.feature.mytodos

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import org.junit.Rule
import org.junit.Test

class MyTodosScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val errorMessage by lazy {
        composeTestRule.activity.getString(R.string.feature_mytodos_error_list)
    }

    private val sampleTodos = listOf(
        TodoUiModel(id = "1", name = "Buy groceries", isDone = false),
        TodoUiModel(id = "2", name = "Finish project", isDone = true)
    )

    @Test
    fun errorState_showsErrorMessage() {
        composeTestRule.setContent {
            TodoScreen(
                state = TodoState.Error("Something went wrong"),
                onAddTodoClick = {},
                onEditTodoClick = {},
                onDeleteTodoClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

    @Test
    fun successState_showsTodosAndAddButton() {
        composeTestRule.setContent {
            TodoScreen(
                state = TodoState.Success(todos = sampleTodos),
                onAddTodoClick = {},
                onEditTodoClick = {},
                onDeleteTodoClick = {}
            )
        }

        sampleTodos.forEach { todo ->
            composeTestRule
                .onNodeWithText(todo.name)
                .assertExists()
                .assertHasClickAction()
        }

        composeTestRule
            .onNodeWithContentDescription("Add Todo")
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun successState_whenTodoIsDone_showsCheckIcon() {
        composeTestRule.setContent {
            TodoScreen(
                state = TodoState.Success(todos = sampleTodos),
                onAddTodoClick = {},
                onEditTodoClick = {},
                onDeleteTodoClick = {}
            )
        }

        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasText(sampleTodos[1].name))

        composeTestRule
            .onNodeWithText(sampleTodos[1].name)
            .assertExists()
    }

    @Test
    fun clickingTodoItem_triggersEditClick() {
        var clickedId: String? = null
        val todo = sampleTodos.first()

        composeTestRule.setContent {
            TodoScreen(
                state = TodoState.Success(todos = sampleTodos),
                onAddTodoClick = {},
                onEditTodoClick = { clickedId = it },
                onDeleteTodoClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(todo.name)
            .performClick()

        assert(clickedId == todo.id)
    }

    @Test
    fun loadingState_showsLoadingWheel() {
        composeTestRule.setContent {
            Box {
                TodoScreen(
                    state = TodoState.Loading,
                    onAddTodoClick = {},
                    onEditTodoClick = {},
                    onDeleteTodoClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("TodoLoadingWheel")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun longClickingTodoItem_triggersDeleteClick() {
        var deletedTodo: TodoUiModel? = null
        val todo = sampleTodos.first()

        composeTestRule.setContent {
            TodoScreen(
                state = TodoState.Success(todos = sampleTodos),
                onAddTodoClick = {},
                onEditTodoClick = {},
                onDeleteTodoClick = { deletedTodo = it }
            )
        }

        composeTestRule
            .onNodeWithText(todo.name)
            .performTouchInput { longClick() }

        assert(deletedTodo == todo)
    }
}
