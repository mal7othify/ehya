/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.arslan_wessam
import com.eillia.ehya.resources.tajawal_bold
import com.eillia.ehya.resources.tajawal_extra_bold
import com.eillia.ehya.resources.tajawal_light
import com.eillia.ehya.resources.tajawal_medium
import com.eillia.ehya.resources.tajawal_regular
import org.jetbrains.compose.resources.Font

@Composable
fun tajawalFontFamily(): FontFamily = FontFamily(
  Font(Res.font.tajawal_light, FontWeight.Light),
  Font(Res.font.tajawal_regular, FontWeight.Normal),
  Font(Res.font.tajawal_medium, FontWeight.Medium),
  Font(Res.font.tajawal_bold, FontWeight.Bold),
  Font(Res.font.tajawal_extra_bold, FontWeight.ExtraBold)
)

@Composable
fun arslanWessamFontFamily(): FontFamily = FontFamily(Font(Res.font.arslan_wessam))

@Composable
fun ehyaTypography(): Typography {
  val tajawal = tajawalFontFamily()
  val defaults = Typography()
  return Typography(
    displaySmall = defaults.displaySmall.copy(fontFamily = tajawal, fontWeight = FontWeight.Bold),
    headlineLarge = defaults.headlineLarge.copy(fontFamily = tajawal, fontWeight = FontWeight.Bold),
    headlineMedium =
    defaults.headlineMedium.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.Bold
    ),
    headlineSmall =
    defaults.headlineSmall.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
      lineHeight = 1.5.em,
      textAlign = TextAlign.Center
    ),
    titleLarge =
    defaults.titleLarge.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.ExtraBold
    ),
    titleMedium =
    defaults.titleMedium.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.Bold,
      fontSize = 17.sp,
      lineHeight = 1.4.em
    ),
    titleSmall = defaults.titleSmall.copy(fontFamily = tajawal, fontWeight = FontWeight.Medium),
    bodyLarge =
    defaults.bodyLarge.copy(
      fontFamily = tajawal,
      fontSize = 16.sp,
      lineHeight = 1.7.em,
      textAlign = TextAlign.Justify
    ),
    bodyMedium =
    defaults.bodyMedium.copy(
      fontFamily = tajawal,
      fontSize = 14.sp,
      lineHeight = 1.6.em
    ),
    bodySmall =
    defaults.bodySmall.copy(
      fontFamily = tajawal,
      fontSize = 12.sp,
      lineHeight = 1.5.em
    ),
    labelLarge =
    defaults.labelLarge.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.Medium,
      fontSize = 14.sp
    ),
    labelMedium =
    defaults.labelMedium.copy(
      fontFamily = tajawal,
      fontWeight = FontWeight.Medium
    ),
    labelSmall = defaults.labelSmall.copy(fontFamily = tajawal)
  )
}

/** Decorative style for the category ribbon on a card, set in the Arslan Wessam face. */
@Composable
fun categoryTextStyle(): TextStyle = TextStyle(
  fontFamily = arslanWessamFontFamily(),
  fontSize = 22.sp,
  color = BlackOlive
)
