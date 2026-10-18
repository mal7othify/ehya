/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.sunan

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.choose_sunnah
import com.eillia.ehya.resources.hadith
import com.eillia.ehya.resources.howto
import com.eillia.ehya.resources.share_sunnah
import com.eillia.ehya.ui.components.EhyaTopBar
import com.eillia.ehya.ui.components.LoadingLogo
import com.eillia.ehya.ui.components.SunnahIllustration
import com.eillia.ehya.ui.components.contentEnter
import com.eillia.ehya.ui.components.loadingExit
import com.eillia.ehya.ui.theme.BlackOlive
import com.eillia.ehya.ui.theme.CardGradient
import com.eillia.ehya.ui.theme.Dimens
import com.eillia.ehya.ui.theme.LocalCategoryTextStyle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Full detail of one sunnah. On phones it is a page with a back arrow; in the list-detail layout
 * of larger windows it is the second pane and [onBack] is null.
 *
 * Unlike the play card, this is a reading page: the header sits on the window background and the
 * hadith and how-to each get their own rounded section, with nothing collapsed.
 */
@Composable
fun SunnahDetailScreen(
  sunnahId: Int,
  onBack: (() -> Unit)?,
  modifier: Modifier = Modifier,
  viewModel: SunnahDetailViewModel =
    koinViewModel(key = "sunnah-$sunnahId") { parametersOf(sunnahId) }
) {
  val sunnah by viewModel.sunnah.collectAsStateWithLifecycle()
  Scaffold(
    modifier = modifier,
    containerColor = Color.Transparent,
    // Window insets are already applied by the app scaffold around the NavDisplay.
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      EhyaTopBar(
        title = sunnah?.category?.title ?: "",
        onBack = onBack,
        windowInsets = WindowInsets(0, 0, 0, 0),
        actions = {
          IconButton(onClick = viewModel::share, enabled = sunnah != null) {
            Icon(Icons.Rounded.Share, contentDescription = stringResource(Res.string.share_sunnah))
          }
        }
      )
    }
  ) { innerPadding ->
    AnimatedContent(
      targetState = sunnah,
      transitionSpec = {
        if (initialState == null) {
          contentEnter() togetherWith loadingExit()
        } else {
          fadeIn() togetherWith fadeOut()
        }
      },
      contentKey = { it?.id },
      contentAlignment = Alignment.TopCenter,
      label = "detail",
      modifier = Modifier.fillMaxSize().padding(innerPadding)
    ) { current ->
      if (current == null) {
        LoadingLogo()
      } else {
        SunnahDetailContent(
          sunnah = current,
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        )
      }
    }
  }
}

@Composable
fun SunnahDetailContent(sunnah: Sunnah, modifier: Modifier = Modifier) {
  Box(modifier, contentAlignment = Alignment.TopCenter) {
    Column(
      modifier = Modifier
        .widthIn(max = Dimens.MaxContentWidth)
        .fillMaxWidth()
        .padding(horizontal = Dimens.PaddingNormal)
        .padding(bottom = Dimens.PaddingLarge),
      verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)
    ) {
      SunnahHeader(sunnah)
      DetailSection(title = stringResource(Res.string.hadith)) {
        // A short quotation reads better ragged than justified.
        Text(
          text = sunnah.hadith,
          style = MaterialTheme.typography.bodyLarge,
          color = BlackOlive,
          textAlign = TextAlign.Start,
          modifier = Modifier.fillMaxWidth()
        )
        val grading = sunnah.strength.split('|').map { it.trim() }.filter { it.isNotEmpty() }
        if (grading.isNotEmpty()) {
          HorizontalDivider(
            color = BlackOlive.copy(alpha = 0.15f),
            modifier = Modifier.padding(vertical = Dimens.PaddingSmall)
          )
          grading.forEach { line ->
            Text(
              text = line,
              style = MaterialTheme.typography.bodySmall,
              color = BlackOlive.copy(alpha = 0.8f),
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
      DetailSection(title = stringResource(Res.string.howto)) {
        Text(
          text = sunnah.howto,
          style = MaterialTheme.typography.bodyLarge,
          color = BlackOlive,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}

/** Illustration, title and the category and frequency tags, on the window background. */
@Composable
private fun SunnahHeader(sunnah: Sunnah) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
    modifier = Modifier.fillMaxWidth().padding(top = Dimens.PaddingSmall)
  ) {
    SunnahIllustration(
      category = sunnah.category.key,
      modifier = Modifier.size(Dimens.DetailIllustrationSize)
    )
    Text(
      text = sunnah.title,
      style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp, lineHeight = 1.4.em),
      color = MaterialTheme.colorScheme.onBackground,
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = sunnah.category.title,
        style = LocalCategoryTextStyle.current,
        modifier = Modifier
          .clip(MaterialTheme.shapes.extraSmall)
          .background(MaterialTheme.colorScheme.primary)
          .padding(horizontal = 12.dp, vertical = 2.dp)
      )
      sunnah.quantity?.takeIf { it.isNotBlank() }?.let { quantity ->
        Text(
          text = quantity,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onBackground,
          modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 12.dp, vertical = 6.dp)
        )
      }
    }
    Spacer(Modifier.height(Dimens.PaddingSmall))
  }
}

/** A titled, rounded block on the card gradient, the same corners as the play card. */
@Composable
private fun DetailSection(title: String, content: @Composable ColumnScope.() -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.large)
      .background(CardGradient)
      .padding(Dimens.PaddingNormal),
    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = BlackOlive
    )
    content()
  }
}

/** Shown in the detail pane of wide layouts until a sunnah is picked. */
@Composable
fun SunnahDetailPlaceholder(modifier: Modifier = Modifier) {
  Box(modifier.fillMaxSize().padding(Dimens.PaddingLarge), contentAlignment = Alignment.Center) {
    Text(
      text = stringResource(Res.string.choose_sunnah),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      textAlign = TextAlign.Center
    )
  }
}
