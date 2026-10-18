/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.ic_logo
import com.eillia.ehya.resources.logo
import com.eillia.ehya.ui.theme.Dimens
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The logo shown while data loads. It starts at rest size so it lines up with the static system
 * launch screen on Android and iOS, then squashes and springs back past its size, as if the launch
 * logo itself had come alive, and keeps breathing gently until the content is ready.
 */
@Composable
fun LoadingLogo(modifier: Modifier = Modifier) {
  val scale = remember { Animatable(1f) }
  val breath = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    // Rest first: the first frames may be drawn behind the system splash, and the logo must still
    // match it when the splash lifts.
    delay(REST_MILLIS)
    scale.animateTo(0.8f, tween(durationMillis = 140, easing = FastOutSlowInEasing))
    scale.animateTo(
      targetValue = 1f,
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
      )
    )
    while (true) {
      breath.animateTo(1f, tween(durationMillis = 1_000, easing = EaseInOutSine))
      breath.animateTo(0f, tween(durationMillis = 1_000, easing = EaseInOutSine))
    }
  }
  Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(Res.drawable.ic_logo),
      contentDescription = stringResource(Res.string.logo),
      modifier = Modifier
        .size(Dimens.SplashLogoSize)
        .graphicsLayer {
          val s = scale.value * (1f + BREATH_SCALE * breath.value)
          scaleX = s
          scaleY = s
          translationY = -BREATH_LIFT_DP * density * breath.value
        }
    )
  }
}

private const val REST_MILLIS = 220L
private const val BREATH_SCALE = 0.05f
private const val BREATH_LIFT_DP = 6f

/** Content arriving after [LoadingLogo]: rises into place with a soft overshoot while fading in. */
fun contentEnter(): EnterTransition = fadeIn(tween(durationMillis = 350)) +
  scaleIn(
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    initialScale = 0.85f
  ) +
  slideInVertically(
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    )
  ) { it / 3 }

/** [LoadingLogo] leaving: swells slightly and fades, so the content appears to grow out of it. */
fun loadingExit(): ExitTransition = fadeOut(tween(durationMillis = 220)) +
  scaleOut(tween(durationMillis = 220, easing = FastOutSlowInEasing), targetScale = 1.25f)

/**
 * Keeps the logo on screen long enough for its bounce to finish the first time the app starts.
 * Data usually arrives within a few hundred milliseconds, which would cut the motion short. Later
 * visits to a loading screen are not held.
 */
@Composable
fun rememberLaunchHold(): Boolean {
  var holding by remember { mutableStateOf(!launchHoldDone) }
  LaunchedEffect(Unit) {
    if (holding) {
      delay(LAUNCH_HOLD_MILLIS)
      launchHoldDone = true
      holding = false
    }
  }
  return holding
}

private var launchHoldDone = false
private const val LAUNCH_HOLD_MILLIS = 1_100L
