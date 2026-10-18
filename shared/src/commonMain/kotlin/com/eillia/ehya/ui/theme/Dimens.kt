/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.theme

import androidx.compose.ui.unit.dp

object Dimens {
  val PaddingSmall = 8.dp
  val PaddingNormal = 16.dp
  val PaddingLarge = 24.dp
  val IllustrationSize = 180.dp
  val LogoSize = 160.dp

  /** Cards and reading columns never grow wider than this, whatever the window size. */
  val MaxContentWidth = 560.dp

  /** On medium and expanded windows the play card floats instead of filling the height. */
  val MaxCardHeight = 720.dp

  /** How far each card behind the top one peeks out above it. */
  val StackPeek = 14.dp

  /** Minimum width of a tile in the sunan grid; the grid adds columns as the window grows. */
  val SunnahTileMinWidth = 320.dp
}
