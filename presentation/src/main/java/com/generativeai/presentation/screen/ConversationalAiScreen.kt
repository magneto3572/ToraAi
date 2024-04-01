package com.generativeai.presentation.screen


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.generativeai.presentation.component.BottomSearch
import com.generativeai.presentation.component.MainConversationArea
import com.generativeai.presentation.component.TopBar
import com.generativeai.presentation.intent.ConversationalIntent
import com.generativeai.presentation.uistate.ConversationalAiUiState
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.ui.theme.spacing_16
import com.generativeai.presentation.viewmodel.ConversationalAiViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun ConversationAiScreenRoute(
    navController: NavController,
    viewModel: ConversationalAiViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConversationsAiScreen(
        navController,
        uiState = uiState,
        onConversationIntent = viewModel::acceptIntent,
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ConversationsAiScreen(
    navController: NavController,
    uiState: ConversationalAiUiState,
    onConversationIntent: (ConversationalIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing_16),
    ) {
        TopBar {
            onConversationIntent(ConversationalIntent.ClearAll)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            MainConversationArea(
                message = uiState.data.responseMessage,
                isPromptSuggestionVisible = uiState.data.responseMessage.text.isEmpty(),
                onItemClick = {
                    onConversationIntent(ConversationalIntent.OnPromptClick(it))
                },
                promptSuggestionList = uiState.data.promptSuggestionList
                    ?: emptyList<PromptItem>() as ArrayList<PromptItem>,
            )
        }

        BottomSearch(
            modelIn = Constants.CONVERSATIONAL,
            isPickerVisible = uiState.data.isSearchWithImage,
            query = uiState.data.query,
            onSendClick = {
                onConversationIntent(ConversationalIntent.OnSendClick(it))
            },
            onImagePickerClick = {

            },
        ) {
            // This is trailing lambda for onValue change higher order function
            onConversationIntent(ConversationalIntent.OnValueChange(it))
        }
    }
}