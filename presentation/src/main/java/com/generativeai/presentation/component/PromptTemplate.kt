package com.generativeai.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.generativeai.domain.model.PromptItem
import com.generativeai.presentation.R
import kotlinx.parcelize.RawValue


@Composable
fun PromptTemplate(
    onItemClick: (PromptItem) -> Unit,
    promptSuggestionList: @RawValue ArrayList<PromptItem>,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(promptSuggestionList.size) { index ->
            ItemBox(
                promptSuggestionList[index],
                onItemClick,
            )
        }
    }
}

@Composable
fun ItemBox(
    promptItem: PromptItem,
    onItemClick: (PromptItem) -> Unit,
) {
    Card(
        modifier = Modifier.heightIn(min = 160.dp),
        onClick = {
            onItemClick.invoke(promptItem)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = promptItem.heading.toString(),
                textAlign = TextAlign.Start,
            )
            Text(
                style = MaterialTheme.typography.titleSmall,
                text = promptItem.subheading.toString(),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp),
                textAlign = TextAlign.Start,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!promptItem.url.isNullOrBlank()) {
                    AsyncImage(
                        contentScale = ContentScale.FillBounds,
                        model = promptItem.url,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(40.dp)
                            .clip(shape = MaterialTheme.shapes.small)
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.write_prompt),
                    contentDescription = "Template Item Icon",
                    modifier = Modifier
                        .padding(6.dp)
                )
            }
        }
    }
}