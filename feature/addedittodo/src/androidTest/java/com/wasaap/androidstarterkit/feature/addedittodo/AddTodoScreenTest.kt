package com.wasaap.androidstarterkit.feature.addedittodo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AddTodoScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val errorMessage = "Something went wrong"
    private val addTitle by lazy { composeTestRule.activity.getString(R.string.feature_addedittodo_add_todo) }
    private val editTitle by lazy { composeTestRule.activity.getString(R.string.feature_addedittodo_edit_todo) }
    private val nameLabel by lazy { composeTestRule.activity.getString(R.string.feature_addedittodo_name) }
    private val completedLabel by lazy { composeTestRule.activity.getString(R.string.feature_addedittodo_completed) }
    private val saveLabel by lazy { composeTestRule.activity.getString(R.string.feature_addedittodo_save) }

    @Test
    fun loadingState_showsLoadingWheel() {
        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(isLoading = true),
                onNameChanged = {},
                onDoneChanged = {},
                onSave = {}
            )
        }

        composeTestRule
            .onNodeWithTag("TodoLoadingWheel")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() {
        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(error = errorMessage),
                onNameChanged = {},
                onDoneChanged = {},
                onSave = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun addMode_showsAddTitle_andFields() {
        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(
                    isLoading = false,
                    name = "",
                    done = false,
                    isEditMode = false
                ),
                onNameChanged = {},
                onDoneChanged = {},
                onSave = {}
            )
        }

        composeTestRule
            .onNodeWithText(addTitle)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(nameLabel)
            .assertExists()

        composeTestRule
            .onNodeWithText(completedLabel)
            .assertExists()

        composeTestRule
            .onNodeWithText(saveLabel)
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun editMode_showsEditTitle_andPrefilledFields() {
        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(
                    isLoading = false,
                    name = "Buy groceries",
                    done = true,
                    isEditMode = true
                ),
                onNameChanged = {},
                onDoneChanged = {},
                onSave = {}
            )
        }

        composeTestRule
            .onNodeWithText(editTitle)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Buy groceries")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun enteringName_andClickingSave_triggersCallbacks() {
        var nameChanged: String? = null
        var doneChanged: Boolean? = null
        var saveClicked = false

        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(
                    isLoading = false,
                    name = "",
                    done = false,
                    isEditMode = false
                ),
                onNameChanged = { nameChanged = it },
                onDoneChanged = { doneChanged = it },
                onSave = { saveClicked = true }
            )
        }

        composeTestRule
            .onNodeWithText(nameLabel)
            .performTextInput("New Todo")

        composeTestRule
            .onNodeWithText(saveLabel)
            .performClick()

        assert(nameChanged == "New Todo")
        assert(saveClicked)
    }

    @Test
    fun completedCheckbox_canBeToggled() {
        var toggled: Boolean? = null

        composeTestRule.setContent {
            AddTodoContent(
                uiState = AddTodoUiState(
                    isLoading = false,
                    name = "Test Todo",
                    done = false,
                    isEditMode = false
                ),
                onNameChanged = {},
                onDoneChanged = { toggled = it },
                onSave = {}
            )
        }

        composeTestRule
            .onNodeWithTag("todo_completed_checkbox")
            .performClick()

        assert(toggled == true)
    }
}