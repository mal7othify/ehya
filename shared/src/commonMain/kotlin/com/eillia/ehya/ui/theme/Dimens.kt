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

  /** The illustration in the detail page header, smaller than on a play card. */
  val DetailIllustrationSize = 132.dp
  val LogoSize = 160.dp

  /**
   * Box for the loading logo. The artwork fills about 45% of it, which puts the visible logo at
   * ~118dp: the same size the Android 12 system splash draws its icon, so the hand-over does not
   * jump. The iOS launch image is sized to match (see iosApp LaunchLogo.imageset).
   */
  val SplashLogoSize = 262.dp

  /** Cards and reading columns never grow wider than this, whatever the window size. */
  val MaxContentWidth = 560.dp

  /** On medium and expanded windows the play card floats instead of filling the height. */
  val MaxCardHeight = 720.dp

  /** How far each card behind the top one peeks out above it. */
  val StackPeek = 14.dp

  /** Minimum width of a tile in the sunan grid; the grid adds columns as the window grows. */
  val SunnahTileMinWidth = 320.dp
}
