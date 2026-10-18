/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.ic_logo
import com.eillia.ehya.resources.logo
import com.eillia.ehya.ui.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** The logo popping in while data loads. Replaces the splash "screen" that used to sit in the back stack. */
@Composable
fun LoadingLogo(modifier: Modifier = Modifier) {
  val scale = remember { Animatable(0.4f) }
  LaunchedEffect(Unit) {
    scale.animateTo(1f, tween(durationMillis = 700, easing = FastOutSlowInEasing))
  }
  Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(Res.drawable.ic_logo),
      contentDescription = stringResource(Res.string.logo),
      modifier = Modifier.size(Dimens.LogoSize).scale(scale.value)
    )
  }
}
