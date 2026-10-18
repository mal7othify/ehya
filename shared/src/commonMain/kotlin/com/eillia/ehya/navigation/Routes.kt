/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/** Every destination of the app. Keys are serializable so the back stack survives process death. */
@Serializable
sealed interface Route : NavKey

/** Destinations that have their own entry in the navigation bar / rail. */
sealed interface TopLevelRoute : Route

@Serializable
data object PlayRoute : TopLevelRoute

@Serializable
data object SunanRoute : TopLevelRoute

@Serializable
data class SunnahDetailRoute(val sunnahId: Int) : Route

@Serializable
data object InfoRoute : Route

val topLevelRoutes: List<TopLevelRoute> = listOf(PlayRoute, SunanRoute)

/** Explicit polymorphic registration: reflection based serialization is not available on iOS or web. */
val navigationSavedStateConfiguration =
  SavedStateConfiguration {
    serializersModule =
      SerializersModule {
        polymorphic(NavKey::class) {
          subclass(PlayRoute::class)
          subclass(SunanRoute::class)
          subclass(SunnahDetailRoute::class)
          subclass(InfoRoute::class)
        }
      }
  }
