package com.generativeai.presentation.component

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.generativeai.presentation.R
import com.generativeai.presentation.ui.theme.spacing_16


@Composable
fun HorizontalImageList(
    bitmaps: List<Bitmap>,
    isCloseVisible: Boolean,
    onCloseClick: (Int) -> Unit = {}
) {
    LazyRow(
        modifier = Modifier.padding(top = spacing_16)
    ) {
        itemsIndexed(bitmaps){ index, bitmap ->
            ImageItem(bitmap = bitmap, index, isCloseVisible = isCloseVisible, onCloseClick)
        }
    }
}


@Composable
fun ImageItem(
    bitmap: Bitmap,
    index : Int,
    isCloseVisible: Boolean,
    onCloseClick : (Int) -> Unit
    ) {
    Box {
        AsyncImage(
            contentScale = ContentScale.FillBounds,
            model = bitmap,
            contentDescription = null,
            modifier = Modifier
                .padding(4.dp)
                .size(120.dp)
                .clip(shape = MaterialTheme.shapes.small)
        )
        if (isCloseVisible) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clickable {
                        onCloseClick.invoke(index)
                    },
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
            )
        }
    }
}