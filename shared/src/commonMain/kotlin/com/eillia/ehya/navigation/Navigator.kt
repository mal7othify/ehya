/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Owns the single back stack of the app (Navigation 3 leaves that to us) and encodes the few
 * navigation rules we have: [PlayRoute] is always the root, top level destinations replace each
 * other instead of piling up, and everything else is pushed on top.
 */
@Stable
class Navigator(val backStack: NavBackStack<NavKey>) {
  val current: NavKey? get() = backStack.lastOrNull()

  /** The top level section the user is in, used to highlight the right navigation item. */
  val currentTopLevel: TopLevelRoute
    get() = backStack.lastOrNull { it is TopLevelRoute } as? TopLevelRoute ?: PlayRoute

  val canGoBack: Boolean get() = backStack.size > 1

  fun navigateTo(route: Route) {
    if (backStack.lastOrNull() != route) backStack.add(route)
  }

  fun selectTopLevel(route: TopLevelRoute) {
    backStack.clear()
    backStack.add(PlayRoute)
    if (route != PlayRoute) backStack.add(route)
  }

  fun goBack() {
    if (canGoBack) backStack.removeLastOrNull()
  }
}

@Composable
fun rememberNavigator(): Navigator {
  val backStack = rememberNavBackStack(navigationSavedStateConfiguration, PlayRoute)
  return remember(backStack) { Navigator(backStack) }
}
