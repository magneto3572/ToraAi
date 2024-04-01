package com.generativeai.domain.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

fun getBitmapFromURL(src: String?): Bitmap? {
    runCatching {
        val url = URL(src)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        val myBitmap = BitmapFactory.decodeStream(input)
        return myBitmap
    }.getOrElse {
        it.printStackTrace()
    }
    return null
}