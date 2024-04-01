package com.generativeai.presentation.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.generativeai.presentation.R


val dmSansFamily = FontFamily(
    Font(R.font.dm_sans_regular, FontWeight.Normal),
    Font(R.font.dm_sans_medium, FontWeight.Medium),
    Font(R.font.dm_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.dm_sans_bold, FontWeight.Bold)
)

val HeadingXXLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 28.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.2).sp
)

val HeadingXLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    letterSpacing = (-0.2).sp
)

val HeadingLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.2).sp
)

val HeadingMedium = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.2).sp
)

val HeadingSmall = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.1).sp
)

val HeadingXSmall = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 14.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.1).sp
)

val HeadingXXSmall = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontFamily = dmSansFamily,
    fontSize = 14.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

// Subheading
val SubHeadingLarge = TextStyle(
    fontWeight = FontWeight.Medium,
    fontFamily = dmSansFamily,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = -(0.1).sp
)

val SubHeadingMedium = TextStyle(
    fontWeight = FontWeight.Medium,
    fontFamily = dmSansFamily,
    fontSize = 14.sp,
    lineHeight = 22.sp,
    letterSpacing = -(0.1).sp
)

val SubHeadingRegular = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = dmSansFamily,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

// Paragraph
val ParagraphLarge = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = dmSansFamily,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

val ParagraphMedium = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = dmSansFamily,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = 0.sp
)

val ParagraphRegular = TextStyle(
    fontWeight = FontWeight.Normal,
    fontFamily = dmSansFamily,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

// Label
val LabelXLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
)

val LabelLarge = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = dmSansFamily,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = 0.sp
)

val LabelMedium = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontFamily = dmSansFamily,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

val LabelSmall = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontFamily = dmSansFamily,
    fontSize = 14.sp,
    lineHeight = 22.sp,
    letterSpacing = (0.1).sp
)

val LabelXSmall = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontFamily = dmSansFamily,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = (0.1).sp
)

val LabelXXSmall = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontFamily = dmSansFamily,
    fontSize = 10.sp,
    lineHeight = 22.sp,
    letterSpacing = (0.1).sp
)