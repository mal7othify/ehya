/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.about
import com.eillia.ehya.resources.app_name
import com.eillia.ehya.resources.ic_logo
import com.eillia.ehya.resources.info
import com.eillia.ehya.resources.logo
import com.eillia.ehya.resources.play
import com.eillia.ehya.resources.playsteps
import com.eillia.ehya.resources.share_app
import com.eillia.ehya.resources.share_app_prompt
import com.eillia.ehya.resources.shareapp
import com.eillia.ehya.resources.version
import com.eillia.ehya.ui.components.EhyaTopBar
import com.eillia.ehya.ui.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InfoScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: InfoViewModel = koinViewModel()
) {
  val shareTemplate = stringResource(Res.string.shareapp, "%s")
  Scaffold(
    modifier = modifier,
    containerColor = Color.Transparent,
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      EhyaTopBar(
        title = stringResource(Res.string.info),
        onBack = onBack,
        windowInsets = WindowInsets(0, 0, 0, 0)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(Dimens.PaddingNormal),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Column(
        modifier = Modifier.widthIn(max = Dimens.MaxContentWidth),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(Res.string.app_name),
          style = MaterialTheme.typography.headlineMedium,
          textAlign = TextAlign.Center
        )
        Image(
          painter = painterResource(Res.drawable.ic_logo),
          contentDescription = stringResource(Res.string.logo),
          modifier = Modifier.size(Dimens.LogoSize)
        )
        Text(
          text = stringResource(Res.string.about),
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth().padding(Dimens.PaddingNormal)
        )
        Text(
          text = stringResource(Res.string.play),
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
        Text(
          text = stringResource(Res.string.playsteps),
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.padding(Dimens.PaddingNormal)
        )
        Spacer(Modifier.height(Dimens.PaddingLarge))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(Res.string.share_app_prompt),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
          )
          IconButton(onClick = { viewModel.shareApp { url -> shareTemplate.replace("%s", url) } }) {
            Icon(
              imageVector = Icons.Rounded.Share,
              contentDescription = stringResource(Res.string.share_app),
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
        Spacer(Modifier.height(Dimens.PaddingLarge))
        Text(
          text = stringResource(Res.string.version, viewModel.appInfo.versionName),
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}
