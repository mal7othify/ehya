/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.eillia.ehya.data.model.CategoryKey
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.appearance
import com.eillia.ehya.resources.food
import com.eillia.ehya.resources.general
import com.eillia.ehya.resources.pray
import com.eillia.ehya.resources.sleep
import com.eillia.ehya.resources.wudoo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

fun CategoryKey.illustration(): DrawableResource = when (this) {
  CategoryKey.WUDOO -> Res.drawable.wudoo
  CategoryKey.PRAY -> Res.drawable.pray
  CategoryKey.SLEEP -> Res.drawable.sleep
  CategoryKey.FOOD -> Res.drawable.food
  CategoryKey.APPEARANCE -> Res.drawable.appearance
  CategoryKey.GENERAL -> Res.drawable.general
}

@Composable
fun SunnahIllustration(category: CategoryKey, modifier: Modifier = Modifier) {
  Image(
    painter = painterResource(category.illustration()),
    contentDescription = null,
    contentScale = ContentScale.Fit,
    modifier = modifier
  )
}
