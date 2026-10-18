/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection

val LocalCategoryTextStyle = staticCompositionLocalOf { TextStyle.Default }

/** Material 3 theme with the Ehya palette and fonts. The app is Arabic only, so it is always RTL. */
@Composable
fun EhyaTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = EhyaColorScheme,
    typography = ehyaTypography(),
    shapes = EhyaShapes
  ) {
    CompositionLocalProvider(
      LocalLayoutDirection provides LayoutDirection.Rtl,
      LocalCategoryTextStyle provides categoryTextStyle(),
      content = content
    )
  }
}
