/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.sunan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.choose_sunnah
import com.eillia.ehya.resources.share_sunnah
import com.eillia.ehya.ui.components.EhyaTopBar
import com.eillia.ehya.ui.components.LoadingLogo
import com.eillia.ehya.ui.components.SunnahContent
import com.eillia.ehya.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Full detail of one sunnah. On phones it is a page with a back arrow; in the list-detail layout
 * of larger windows it is the second pane and [onBack] is null.
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
    Box(
      modifier = Modifier.fillMaxSize().padding(innerPadding),
      contentAlignment = Alignment.TopCenter
    ) {
      val current = sunnah
      if (current == null) {
        LoadingLogo()
      } else {
        SunnahContent(
          sunnah = current,
          modifier = Modifier
            .widthIn(max = Dimens.MaxContentWidth)
            .padding(horizontal = Dimens.PaddingNormal)
            .clip(MaterialTheme.shapes.large)
        )
      }
    }
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
