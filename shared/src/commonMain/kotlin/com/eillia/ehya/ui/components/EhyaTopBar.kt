/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.app_name
import com.eillia.ehya.resources.back
import com.eillia.ehya.resources.ic_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Transparent top bar with the logo. Shows a back arrow when [onBack] is given. */
@Composable
fun EhyaTopBar(
  modifier: Modifier = Modifier,
  title: String = stringResource(Res.string.app_name),
  onBack: (() -> Unit)? = null,
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  actions: @Composable RowScope.() -> Unit = {}
) {
  TopAppBar(
    modifier = modifier,
    windowInsets = windowInsets,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
          painter = painterResource(Res.drawable.ic_logo),
          contentDescription = null,
          modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
      }
    },
    navigationIcon = {
      if (onBack != null) {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(Res.string.back),
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    },
    actions = actions,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.Transparent,
      titleContentColor = MaterialTheme.colorScheme.onBackground,
      actionIconContentColor = MaterialTheme.colorScheme.primary
    )
  )
}
