/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.repository

import com.eillia.ehya.data.model.Interaction
import com.eillia.ehya.data.model.Sunnah
import kotlinx.coroutines.flow.Flow

interface SunanRepository {
  /** All sunan, seeded on first access. */
  fun observeSunan(): Flow<List<Sunnah>>

  fun observeSunnah(id: Int): Flow<Sunnah?>

  fun observeInteractions(): Flow<List<Interaction>>

  suspend fun recordTry(sunnahId: Int)

  suspend fun recordPass(sunnahId: Int)

  suspend fun setFavorite(sunnahId: Int, favorite: Boolean)
}
