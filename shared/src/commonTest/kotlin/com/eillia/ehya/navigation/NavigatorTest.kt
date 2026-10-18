/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigatorTest {
  private fun navigator() = Navigator(NavBackStack<NavKey>(PlayRoute))

  @Test
  fun startsOnPlayAndCannotGoBack() {
    val navigator = navigator()
    assertEquals(PlayRoute, navigator.current)
    assertEquals(PlayRoute, navigator.currentTopLevel)
    assertFalse(navigator.canGoBack)
  }

  @Test
  fun topLevelDestinationsReplaceEachOther() {
    val navigator = navigator()
    navigator.selectTopLevel(SunanRoute)
    navigator.selectTopLevel(PlayRoute)
    navigator.selectTopLevel(SunanRoute)
    assertEquals(listOf<NavKey>(PlayRoute, SunanRoute), navigator.backStack.toList())
  }

  @Test
  fun detailsStackOnTopOfTheirSectionAndBackPopsThem() {
    val navigator = navigator()
    navigator.selectTopLevel(SunanRoute)
    navigator.navigateTo(SunnahDetailRoute(7))
    assertEquals(SunnahDetailRoute(7), navigator.current)
    assertEquals(SunanRoute, navigator.currentTopLevel)
    assertTrue(navigator.canGoBack)

    navigator.goBack()
    assertEquals(SunanRoute, navigator.current)
  }

  @Test
  fun navigatingToTheCurrentRouteDoesNotDuplicateIt() {
    val navigator = navigator()
    navigator.navigateTo(InfoRoute)
    navigator.navigateTo(InfoRoute)
    assertEquals(2, navigator.backStack.size)
  }

  @Test
  fun reselectingTheSectionPopsItsDetails() {
    val navigator = navigator()
    navigator.selectTopLevel(SunanRoute)
    navigator.navigateTo(SunnahDetailRoute(3))
    navigator.selectTopLevel(SunanRoute)
    assertEquals(SunanRoute, navigator.current)
  }
}
