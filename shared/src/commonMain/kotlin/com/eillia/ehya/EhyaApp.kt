/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.eillia.ehya.navigation.EhyaNavDisplay
import com.eillia.ehya.navigation.InfoRoute
import com.eillia.ehya.navigation.Navigator
import com.eillia.ehya.navigation.PlayRoute
import com.eillia.ehya.navigation.SunanRoute
import com.eillia.ehya.navigation.TopLevelRoute
import com.eillia.ehya.navigation.rememberNavigator
import com.eillia.ehya.navigation.topLevelRoutes
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.info
import com.eillia.ehya.resources.tab_play
import com.eillia.ehya.resources.tab_sunan
import com.eillia.ehya.ui.components.EhyaTopBar
import com.eillia.ehya.ui.theme.EhyaTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** Root of the shared UI. Every platform entry point starts Koin (see `di/`) and then calls this. */
@Composable
fun EhyaApp() {
  EhyaTheme {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
      EhyaScaffold(navigator = rememberNavigator())
    }
  }
}

/**
 * Adaptive chrome: a navigation bar on phones, a rail on tablets and foldables, a drawer on very
 * wide windows. The info page is a focused full-screen page, so the chrome hides there.
 */
@Composable
private fun EhyaScaffold(navigator: Navigator) {
  val current = navigator.current
  val showChrome = current != InfoRoute
  val adaptiveInfo = currentWindowAdaptiveInfoV2()
  val suiteType = if (showChrome) {
    NavigationSuiteScaffoldDefaults.navigationSuiteType(adaptiveInfo)
  } else {
    NavigationSuiteType.None
  }

  NavigationSuiteScaffold(
    navigationSuiteType = suiteType,
    containerColor = MaterialTheme.colorScheme.background,
    navigationItems = {
      topLevelRoutes.forEach { route ->
        NavigationSuiteItem(
          selected = route == navigator.currentTopLevel,
          onClick = { navigator.selectTopLevel(route) },
          icon = { Icon(route.icon(), contentDescription = null) },
          label = { Text(stringResource(route.label())) },
          navigationSuiteType = suiteType
        )
      }
    }
  ) {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        if (current is TopLevelRoute) {
          EhyaTopBar(
            actions = {
              IconButton(onClick = { navigator.navigateTo(InfoRoute) }) {
                Icon(Icons.Rounded.Info, contentDescription = stringResource(Res.string.info))
              }
            }
          )
        }
      }
    ) { innerPadding ->
      EhyaNavDisplay(navigator = navigator, modifier = Modifier.fillMaxSize().padding(innerPadding))
    }
  }
}

private fun TopLevelRoute.icon(): ImageVector = when (this) {
  PlayRoute -> Icons.Rounded.PlayArrow
  SunanRoute -> Icons.AutoMirrored.Rounded.List
}

private fun TopLevelRoute.label(): StringResource = when (this) {
  PlayRoute -> Res.string.tab_play
  SunanRoute -> Res.string.tab_sunan
}
