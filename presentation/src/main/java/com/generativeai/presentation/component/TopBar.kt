package com.generativeai.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.generativeai.presentation.R
import com.generativeai.presentation.ui.theme.spacing_16
import com.generativeai.presentation.ui.theme.spacing_8


@Composable
fun TopBar(
    onNewPromptButtonClick: () -> Unit
) {
    Box(
        Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = spacing_16, bottom = spacing_8)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = "Tora"
                )
                Row {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = "Powered by  "
                    )
                    Image(
                        painter = painterResource(id = R.drawable.gemini_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .width(40.dp)
                    )
                }
            }
            Button(
                onClick = {
                    onNewPromptButtonClick.invoke()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.new_prompt_icon),
                    contentDescription = "New"
                )
            }
        }
    }
}