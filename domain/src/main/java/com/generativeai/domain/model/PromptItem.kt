package com.generativeai.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PromptItem(
    val heading : String? = null,
    val subheading : String? = null,
    val icon : Int? = null,
    var url : String? = null
) : Parcelable
