/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val EhyaShapes =
  Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
  )

/** The play card: rounded on top, flush with the bottom edge of the screen. */
val PlayCardShape =
  RoundedCornerShape(
    topStart = CornerSize(30.dp),
    topEnd = CornerSize(30.dp),
    bottomStart = ZeroCornerSize,
    bottomEnd = ZeroCornerSize
  )

/** List item card: asymmetric corners, as in the original design. */
val SunnahCardShape =
  RoundedCornerShape(
    topStart = ZeroCornerSize,
    topEnd = CornerSize(20.dp),
    bottomStart = ZeroCornerSize,
    bottomEnd = CornerSize(20.dp)
  )
