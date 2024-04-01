package com.generativeai.presentation.component

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.generativeai.domain.model.Message
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.R
import com.generativeai.presentation.ui.theme.spacing_12
import com.generativeai.presentation.ui.theme.spacing_16
import com.generativeai.presentation.ui.theme.spacing_32
import com.generativeai.presentation.ui.theme.spacing_4
import com.generativeai.presentation.ui.theme.spacing_8
import com.generativeai.presentation.ui.theme.tora_global_blue_10
import com.generativeai.presentation.ui.theme.tora_global_blue_100
import com.generativeai.presentation.ui.theme.tora_global_orange_10
import kotlinx.coroutines.delay
import kotlinx.parcelize.RawValue


@Composable
fun MainConversationArea(
    bitmapList: List<Bitmap>? = null,
    message: Message,
    isPromptSuggestionVisible: Boolean,
    onItemClick: (PromptItem) -> Unit,
    promptSuggestionList: @RawValue ArrayList<PromptItem>,
) {
    Box {
        if (isPromptSuggestionVisible) {
            Column {
                Text(
                    text = stringResource(R.string.hey_there),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = stringResource(id = R.string.how_can_i_help),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                )
                PromptTemplate(
                    onItemClick = onItemClick, promptSuggestionList = promptSuggestionList
                )
            }
        } else {
            ChatScreen(bitmapList, message)
        }
    }
}

@Composable
fun ChatScreen(bitmapList: List<Bitmap>?, message: Message) {
    val listState = rememberLazyListState()

    // Use remember to make sure the state survives recomposition
    val chatMessages = remember {
        mutableStateListOf<Message>()
    }

    var combinedString by rememberSaveable {
        mutableStateOf("")
    }

    var typedString by rememberSaveable {
        mutableStateOf("")
    }


    LaunchedEffect(message) {
        combinedString = if (message.role == Constants.GEMINI) {
            combinedString.trimStart() + message.text
        } else {
            ""
        }

        if (chatMessages.isNotEmpty() && chatMessages.last().role == message.role) {
            val lastIndex = chatMessages.lastIndex

            for (index in typedString.length..combinedString.lastIndex) {
                typedString = combinedString.take(index + 1)
                chatMessages[lastIndex] = chatMessages[lastIndex].copy(text = typedString, isGenerating = message.isGenerating)
                delay(2L) // Adjust delay as needed for typing speed
            }
        } else {
            chatMessages.add(message)
        }
    }

    LazyColumn(
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = spacing_16),
    ) {
        itemsIndexed(chatMessages.reversed()) { index, message ->
            ChatItem(bitmapList, message)
        }
    }
}


@Composable
fun ChatItem(bitmapList: List<Bitmap>?, message: Message) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val toast = Toast.makeText(context, stringResource(R.string.copied), Toast.LENGTH_LONG)

    var currentRotation by remember { mutableFloatStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(message.isGenerating) {
        if (message.isGenerating == true) {
            rotation.animateTo(
                targetValue = currentRotation + 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        } else {
            currentRotation = 0f
        }
    }

    Row {
        Image(
            contentDescription = "Avatar",
            modifier = Modifier
                .padding(top = spacing_8, end = spacing_4)
                .size(24.dp)
                .let {
                    if (message.role != Constants.USER) {
                        it.rotate(rotation.value)
                    } else {
                        it
                    }
                },
            painter = painterResource(id = if (message.role == Constants.USER) R.drawable.user_avatar else R.drawable.google_gemini_icon),
        )
        ChatItemBubble(bitmapList, message, message.text, clipboardManager, toast)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatItemBubble(
    bitmapList: List<Bitmap>?,
    message: Message,
    typedText: String,
    clipboardManager: ClipboardManager,
    toast: Toast
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing_8)
            .background(
                color = if (message.role == Constants.USER) tora_global_blue_10 else tora_global_orange_10,
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (message.role == Constants.USER) {
                if (bitmapList != null) {
                    Box(modifier = Modifier.padding(horizontal = spacing_12)) {
                        HorizontalImageList(bitmaps = bitmapList, false)
                    }
                }
            }
            if(message.role != Constants.USER){
               if(message.isGenerating == true){
                   ShimmerLayout(message.isGenerating!!)
               } else{
                   TextComponent(message = message, typedText)
               }
            }else{
                TextComponent(message = message, typedText = typedText)
            }
        }
        Image(painter = painterResource(id = R.drawable.copy),
            contentDescription = "Copy",
            modifier = Modifier
                .padding(spacing_16)
                .combinedClickable {
                    clipboardManager.setText(AnnotatedString(typedText))
                    toast.show()
                })
    }
}

@Composable
private fun ShimmerLayout(showShimmer: Boolean) {
    Column {
        Box (modifier = Modifier
            .padding(vertical = spacing_12, horizontal = spacing_16)
            .fillMaxWidth()
            .height(14.dp)
            .background(
                shimmerLineComponent(
                    targetValue = 1300f,
                    showShimmer = showShimmer
                ),
                shape = MaterialTheme.shapes.extraLarge
            )
        )
        Box (modifier = Modifier
            .padding(start = spacing_16, end = spacing_32, bottom = spacing_16)
            .fillMaxWidth()
            .height(14.dp)
            .background(
                shimmerLineComponent(
                    targetValue = 1300f,
                    showShimmer = showShimmer
                ),
                shape = MaterialTheme.shapes.extraLarge
            )
        )
    }
}

@Composable
private fun TextComponent(message: Message, typedText: String){
    Text(
        text = typedText,
        modifier = Modifier.padding(spacing_16),
        style = MaterialTheme.typography.headlineSmall,
        color = if (message.role == Constants.USER) tora_global_blue_100 else tora_global_blue_100,
    )
}


