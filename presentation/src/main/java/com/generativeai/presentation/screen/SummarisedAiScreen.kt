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
import com.generativeai.presentation.intent.SummariseIntent
import com.generativeai.presentation.uistate.SummarisedAiUiState
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.ui.theme.spacing_16
import com.generativeai.presentation.viewmodel.SummarizeViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun SummarisedAiScreenRoute(
    navController: NavController,
    viewModel: SummarizeViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SummarisedAiScreen(
        navController,
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun SummarisedAiScreen(
    navController: NavController,
    uiState: SummarisedAiUiState,
    onIntent: (SummariseIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing_16),
    ) {
        TopBar {
            onIntent(SummariseIntent.ClearAll)
        }
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom
        ) {
            MainConversationArea(
                message = uiState.data.responseMessage,
                isPromptSuggestionVisible = uiState.data.responseMessage.text.isEmpty(),
                onItemClick = {
                    onIntent(SummariseIntent.OnPromptClick(it))
                },
                promptSuggestionList = uiState.data.promptSuggestionList
                    ?: emptyList<PromptItem>() as ArrayList<PromptItem>,
                )
        }
        BottomSearch(modelIn = Constants.SUMMARISED,
            isPickerVisible = uiState.data.isSearchWithImage,
            query = uiState.data.query.toString(),
            onSendClick = {
                onIntent(SummariseIntent.OnSendClick(it))
            },
            onImagePickerClick = {

            }) {
            // This is trailing lambda for onValue change higher order function
            onIntent(SummariseIntent.OnValueChange(it))
        }
    }
}


