/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data

import com.eillia.ehya.data.model.Interaction
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.data.repository.SunanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSunanRepository(sunan: List<Sunnah>) : SunanRepository {
  private val sunanFlow = MutableStateFlow(sunan)
  val tries = mutableListOf<Int>()
  val passes = mutableListOf<Int>()
  val favorites = mutableMapOf<Int, Boolean>()

  override fun observeSunan(): Flow<List<Sunnah>> = sunanFlow

  override fun observeSunnah(id: Int): Flow<Sunnah?> = sunanFlow.map { list ->
    list.firstOrNull {
      it.id ==
        id
    }
  }

  override fun observeInteractions(): Flow<List<Interaction>> = MutableStateFlow(emptyList())

  override suspend fun recordTry(sunnahId: Int) {
    tries += sunnahId
  }

  override suspend fun recordPass(sunnahId: Int) {
    passes += sunnahId
  }

  override suspend fun setFavorite(sunnahId: Int, favorite: Boolean) {
    favorites[sunnahId] = favorite
  }
}
