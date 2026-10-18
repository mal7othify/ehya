/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.repository

import com.eillia.ehya.data.db.InteractionDao
import com.eillia.ehya.data.db.SunnahDao
import com.eillia.ehya.data.model.Interaction
import com.eillia.ehya.data.model.Sunnah
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class DefaultSunanRepository(
  private val sunnahDao: SunnahDao,
  private val interactionDao: InteractionDao,
  private val seeder: SunanSeeder
) : SunanRepository {
  override fun observeSunan(): Flow<List<Sunnah>> = flow {
    seeder.seedIfEmpty()
    emitAll(sunnahDao.observeAll())
  }

  override fun observeSunnah(id: Int): Flow<Sunnah?> = flow {
    seeder.seedIfEmpty()
    emitAll(sunnahDao.observeById(id))
  }

  override fun observeInteractions(): Flow<List<Interaction>> = interactionDao.observeAll()

  override suspend fun recordTry(sunnahId: Int) = update(sunnahId) {
    copy(isTried = true, tries = tries + 1)
  }

  override suspend fun recordPass(sunnahId: Int) = update(sunnahId) {
    copy(isPassed = true, passes = passes + 1)
  }

  override suspend fun setFavorite(sunnahId: Int, favorite: Boolean) = update(sunnahId) {
    copy(isFavorite = favorite)
  }

  private suspend fun update(sunnahId: Int, transform: Interaction.() -> Interaction) {
    val current = interactionDao.findBySunnahId(sunnahId) ?: Interaction(sunnahId = sunnahId)
    interactionDao.upsert(current.transform())
  }
}
