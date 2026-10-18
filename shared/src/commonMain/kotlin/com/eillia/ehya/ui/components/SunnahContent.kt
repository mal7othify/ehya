/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.howto
import com.eillia.ehya.ui.theme.BlackOlive
import com.eillia.ehya.ui.theme.CardGradient
import com.eillia.ehya.ui.theme.Dimens
import com.eillia.ehya.ui.theme.LocalCategoryTextStyle
import org.jetbrains.compose.resources.stringResource

/**
 * The body of a sunnah card: title, how often, illustration, category ribbon, hadith, its grading
 * and an expandable "how to" section. Shared by the play deck and the detail screen.
 */
@Composable
fun SunnahContent(
  sunnah: Sunnah,
  modifier: Modifier = Modifier,
  scrollable: Boolean = true,
  showBackground: Boolean = true,
  footer: @Composable ColumnScope.() -> Unit = {}
) {
  var howToExpanded by rememberSaveable(sunnah.id) { mutableStateOf(false) }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
    modifier = modifier
      .then(if (showBackground) Modifier.background(CardGradient) else Modifier)
      .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
      .padding(Dimens.PaddingNormal)
  ) {
    Text(
      text = sunnah.title,
      style = MaterialTheme.typography.headlineSmall,
      color = BlackOlive,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
    sunnah.quantity?.takeIf { it.isNotBlank() }?.let { quantity ->
      Text(
        text = quantity,
        style = MaterialTheme.typography.bodyMedium,
        color = BlackOlive,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
    }
    SunnahIllustration(
      category = sunnah.category.key,
      modifier = Modifier.size(Dimens.IllustrationSize).padding(Dimens.PaddingSmall)
    )
    Text(
      text = sunnah.category.title,
      style = LocalCategoryTextStyle.current,
      modifier = Modifier
        .clip(MaterialTheme.shapes.extraSmall)
        .background(MaterialTheme.colorScheme.primary)
        .padding(horizontal = 12.dp, vertical = 2.dp)
    )
    Text(
      text = sunnah.hadith,
      style = MaterialTheme.typography.bodyLarge,
      color = BlackOlive,
      modifier = Modifier.fillMaxWidth()
    )
    Text(
      text = sunnah.strength,
      style = MaterialTheme.typography.bodySmall,
      color = BlackOlive.copy(alpha = 0.8f),
      modifier = Modifier.fillMaxWidth()
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .clickable { howToExpanded = !howToExpanded }
        .padding(Dimens.PaddingSmall),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = stringResource(Res.string.howto),
          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
          color = BlackOlive,
          textAlign = TextAlign.Center
        )
        Icon(
          imageVector = if (howToExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
          contentDescription = null,
          tint = BlackOlive
        )
      }
      AnimatedVisibility(
        visible = howToExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
      ) {
        Text(
          text = sunnah.howto,
          style = MaterialTheme.typography.bodyMedium,
          color = BlackOlive,
          modifier = Modifier.fillMaxWidth().padding(top = Dimens.PaddingSmall)
        )
      }
    }
    footer()
    Spacer(Modifier.height(Dimens.PaddingSmall))
  }
}
