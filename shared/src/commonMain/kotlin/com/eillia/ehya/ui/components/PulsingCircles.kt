/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/** Three concentric circles that breathe behind the try / pass buttons. */
@Composable
fun PulsingCircles(color: Color, modifier: Modifier = Modifier) {
  val transition = rememberInfiniteTransition(label = "pulse")
  val fraction by transition.animateFloat(
    initialValue = 0.2f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
    label = "radius"
  )
  Canvas(modifier) {
    val radius = size.minDimension / 2 * fraction
    drawCircle(color.copy(alpha = 0.25f), radius = radius)
    drawCircle(color.copy(alpha = 0.35f), radius = radius / 2)
    drawCircle(color.copy(alpha = 0.5f), radius = radius / 4)
  }
}
