package com.generativeai.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.R
import com.generativeai.presentation.ui.theme.spacing_8


@Composable
fun BottomSearch(
    modelIn: String,
    query: String,
    isPickerVisible: Boolean,
    onSendClick: (String) -> Unit,
    onImagePickerClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        if (isPickerVisible) {
            IconButton(
                onClick = {
                    onImagePickerClick.invoke()
                },
                modifier = Modifier
                    .padding(end = spacing_8)
                    .width(50.dp)
                    .height(50.dp)
                    .align(alignment = Alignment.CenterVertically),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pick_image_icon),
                    contentDescription = "Pick Image Icon",
                )
            }
        }
        TextField(
            value = query,
            placeholder = {
                Text(
                    stringResource(
                        when (modelIn) {
                            Constants.CONVERSATIONAL -> {
                                R.string.conversational_hint
                            }

                            Constants.IMAGETEXT -> {
                                R.string.image_ai_hint
                            }

                            else -> {
                                R.string.summarize_hint
                            }
                        }
                    )
                )
            },
            onValueChange = {
                onValueChange.invoke(it)
            },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .weight(8f)
        )
        IconButton(
            onClick = {
                if (query.isNotBlank()) {
                    onSendClick.invoke(query)
                }
            },
            modifier = Modifier
                .padding(start = spacing_8)
                .size(50.dp)
                .align(alignment = Alignment.CenterVertically),
            colors = IconButtonDefaults.filledIconButtonColors()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.send),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
