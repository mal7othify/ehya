/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BlackOlive = Color(0xFF363732)
val Blackish = Color(0xFF232421)
val Gainsboro = Color(0xFFDCE1E9)
val Mauve = Color(0xFF816982)
val CameoPink = Color(0xFFD4AFB9)
val CardPink = Color(0xFFD2AFB9)
val CardGrey = Color(0xFFAAAEB3)
val SkyBlue = Color(0xFF53D8FB)
val YellowLight = Color(0xFFFFCE56)
val YellowDark = Color(0xFFC89D22)
val TryGreen = Color(0xFF2E7D32)
val PassRed = Color(0xFFC62828)

/**
 * Ehya has a single brand palette: an olive canvas with yellow accents and soft pink cards.
 * It is used in light and dark mode alike, exactly like the original app.
 */
val EhyaColorScheme: ColorScheme =
  darkColorScheme(
    primary = YellowLight,
    onPrimary = BlackOlive,
    primaryContainer = YellowDark,
    onPrimaryContainer = BlackOlive,
    secondary = Mauve,
    onSecondary = Gainsboro,
    secondaryContainer = Mauve,
    onSecondaryContainer = Gainsboro,
    tertiary = CameoPink,
    onTertiary = BlackOlive,
    tertiaryContainer = CameoPink,
    onTertiaryContainer = BlackOlive,
    background = BlackOlive,
    onBackground = Gainsboro,
    surface = BlackOlive,
    onSurface = Gainsboro,
    surfaceVariant = Blackish,
    onSurfaceVariant = Gainsboro,
    surfaceContainer = Blackish,
    surfaceContainerLow = Blackish,
    surfaceContainerHigh = Blackish,
    surfaceContainerHighest = Blackish,
    surfaceContainerLowest = Blackish,
    inverseSurface = Gainsboro,
    inverseOnSurface = BlackOlive,
    outline = Gainsboro.copy(alpha = 0.4f),
    outlineVariant = Gainsboro.copy(alpha = 0.2f),
    scrim = Color.Black
  )

/** Gradient painted behind every sunnah card. Text on it uses [BlackOlive]. */
val CardGradient: Brush = Brush.verticalGradient(listOf(CardPink, CardGrey))
val CardGradientHorizontal: Brush = Brush.horizontalGradient(listOf(CardGrey, CardPink))
