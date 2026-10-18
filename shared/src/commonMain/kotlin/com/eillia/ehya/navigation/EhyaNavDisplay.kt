/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.eillia.ehya.ui.info.InfoScreen
import com.eillia.ehya.ui.play.PlayScreen
import com.eillia.ehya.ui.sunan.SunanScreen
import com.eillia.ehya.ui.sunan.SunnahDetailPlaceholder
import com.eillia.ehya.ui.sunan.SunnahDetailScreen

/**
 * Maps routes to screens. The sunan list and a sunnah's detail form a list-detail pair, so on
 * wide windows they are shown side by side while phones get the familiar push/pop.
 */
@Composable
fun EhyaNavDisplay(navigator: Navigator, modifier: Modifier = Modifier) {
  val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
  val directive = remember(windowAdaptiveInfo) {
    calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(horizontalPartitionSpacerSize = 0.dp)
  }
  val isTwoPane = directive.maxHorizontalPartitions > 1
  val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

  NavDisplay(
    backStack = navigator.backStack,
    modifier = modifier,
    onBack = { navigator.goBack() },
    sceneStrategies = listOf(listDetailStrategy),
    entryDecorators = listOf(
      rememberSaveableStateHolderNavEntryDecorator(),
      rememberViewModelStoreNavEntryDecorator()
    ),
    entryProvider = entryProvider {
      entry<PlayRoute> { PlayScreen() }
      entry<SunanRoute>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = {
          SunnahDetailPlaceholder()
        })
      ) {
        SunanScreen(onOpenSunnah = { sunnah ->
          navigator.openSunnah(sunnah.id, replaceDetail = isTwoPane)
        })
      }
      entry<SunnahDetailRoute>(metadata = ListDetailSceneStrategy.detailPane()) { route ->
        SunnahDetailScreen(
          sunnahId = route.sunnahId,
          onBack = if (isTwoPane) null else navigator::goBack
        )
      }
      entry<InfoRoute> { InfoScreen(onBack = navigator::goBack) }
    }
  )
}

/** In two-pane mode picking another item swaps the detail instead of stacking details. */
private fun Navigator.openSunnah(id: Int, replaceDetail: Boolean) {
  if (replaceDetail && current is SunnahDetailRoute) goBack()
  navigateTo(SunnahDetailRoute(id))
}
