/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.db

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.eillia.ehya.data.model.Interaction
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {
  @Upsert
  suspend fun upsert(interaction: Interaction)

  @Query("SELECT * FROM interactionTable WHERE sunnahId = :sunnahId ORDER BY interactionId LIMIT 1")
  suspend fun findBySunnahId(sunnahId: Int): Interaction?

  @Query("SELECT * FROM interactionTable ORDER BY sunnahId")
  fun observeAll(): Flow<List<Interaction>>
}
