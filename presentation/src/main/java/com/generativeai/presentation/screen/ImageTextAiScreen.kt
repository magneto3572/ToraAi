package com.generativeai.presentation.screen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.component.BottomSearch
import com.generativeai.presentation.component.HorizontalImageList
import com.generativeai.presentation.component.MainConversationArea
import com.generativeai.presentation.component.TopBar
import com.generativeai.presentation.intent.ImageTextIntent
import com.generativeai.presentation.uistate.ImageTextAiUiState
import com.generativeai.presentation.ui.theme.spacing_16
import com.generativeai.presentation.viewmodel.ImageTextAiViewModel
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun ImageTextAiScreenRoute(
    navController: NavController,
    viewModel: ImageTextAiViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ImageTextAiScreen(
        imageTextAiUiState = uiState,
        onImageTextIntent = viewModel::acceptIntent,
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ImageTextAiScreen(
    imageTextAiUiState: ImageTextAiUiState,
    onImageTextIntent: (ImageTextIntent) -> Unit,
) {

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            onImageTextIntent(ImageTextIntent.SendUriOrUrl(it))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing_16),
    ) {
        TopBar {
            onImageTextIntent(ImageTextIntent.ClearAll)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            MainConversationArea(
                bitmapList = imageTextAiUiState.data.bitmaps,
                message = imageTextAiUiState.data.responseMessage,
                isPromptSuggestionVisible = imageTextAiUiState.data.responseMessage.text.isEmpty(),
                onItemClick = {
                    onImageTextIntent(ImageTextIntent.SendUriOrUrl(it.url.toString(), it))
                    if(imageTextAiUiState.data.bitmaps.isNotEmpty()){
                        onImageTextIntent(ImageTextIntent.OnPromptClick(it))
                    }
                },
                promptSuggestionList = imageTextAiUiState.data.promptSuggestionList
                    ?: emptyList<PromptItem>() as ArrayList<PromptItem>,
            )
        }
        if(imageTextAiUiState.data.responseMessage.text.isEmpty()){
            HorizontalImageList(bitmaps = imageTextAiUiState.data.bitmaps, imageTextAiUiState.data.isCloseBtnVisible) {
                onImageTextIntent(ImageTextIntent.RemoveListItem(it))
            }
        }
        BottomSearch(
            modelIn = Constants.IMAGETEXT,
            isPickerVisible = imageTextAiUiState.data.isSearchWithImage,
            query = imageTextAiUiState.data.query,
            onSendClick = {
                onImageTextIntent(ImageTextIntent.OnSendClick(it, imageTextAiUiState.data.bitmaps))
            },
            onImagePickerClick = {
                onImageTextIntent(ImageTextIntent.ClearAll)
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            onImageTextIntent(ImageTextIntent.OnValueChange(it))
        }
    }
}