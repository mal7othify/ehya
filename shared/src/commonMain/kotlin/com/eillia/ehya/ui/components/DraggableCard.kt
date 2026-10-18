/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.eillia.ehya.ui.play.SwipeDirection
import com.eillia.ehya.ui.theme.PlayCardShape
import kotlin.math.abs
import kotlinx.coroutines.launch

/**
 * A card that can be flicked left or right. Dragging past a quarter of the width commits the
 * swipe: the card flies off and [onSwiped] fires. Shorter drags spring back. The gesture is
 * expressed in screen coordinates, so "right" means right on every platform and layout direction.
 */
@Composable
fun DraggableCard(
  onSwiped: (SwipeDirection) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = PlayCardShape,
  content: @Composable () -> Unit
) {
  BoxWithConstraints(modifier) {
    val density = LocalDensity.current
    val widthPx = with(density) { maxWidth.toPx() }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val dragModifier = if (enabled) {
      Modifier.pointerInput(widthPx) {
        val threshold = widthPx / 4
        val exit = widthPx * 1.5f
        detectDragGestures(
          onDragEnd = {
            scope.launch {
              val x = offsetX.value
              if (abs(x) < threshold) {
                launch { offsetX.animateTo(0f, spring()) }
                launch { offsetY.animateTo(0f, spring()) }
              } else {
                val direction = if (x > 0) SwipeDirection.TRY else SwipeDirection.PASS
                offsetX.animateTo(if (x > 0) exit else -exit, tween(300))
                onSwiped(direction)
                offsetX.snapTo(0f)
                offsetY.snapTo(0f)
              }
            }
          },
          onDragCancel = {
            scope.launch {
              launch { offsetX.animateTo(0f, spring()) }
              launch { offsetY.animateTo(0f, spring()) }
            }
          }
        ) { change, dragAmount: Offset ->
          change.consume()
          scope.launch {
            offsetX.snapTo(offsetX.value + dragAmount.x)
            offsetY.snapTo(offsetY.value + dragAmount.y / 3)
          }
        }
      }
    } else {
      Modifier
    }

    Card(
      shape = shape,
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
      modifier = Modifier
        .fillMaxSize()
        .then(dragModifier)
        .graphicsLayer {
          translationX = offsetX.value
          translationY = offsetY.value
          rotationZ = (offsetX.value / 24f).coerceIn(-14f, 14f)
        }
    ) {
      content()
    }
  }
}
