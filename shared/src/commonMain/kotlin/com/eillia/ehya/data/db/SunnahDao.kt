/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.db

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.eillia.ehya.data.model.Sunnah
import kotlinx.coroutines.flow.Flow

@Dao
interface SunnahDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(sunan: List<Sunnah>)

  @Query("SELECT * FROM sunnahTable ORDER BY id")
  fun observeAll(): Flow<List<Sunnah>>

  @Query("SELECT * FROM sunnahTable WHERE id = :id")
  fun observeById(id: Int): Flow<Sunnah?>

  @Query("SELECT COUNT(*) FROM sunnahTable")
  suspend fun count(): Int
}
