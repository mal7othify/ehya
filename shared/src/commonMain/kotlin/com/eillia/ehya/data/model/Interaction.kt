/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.model

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

/** What the user has done with a given [Sunnah] so far. One row per sunnah. */
@Entity(
  tableName = "interactionTable",
  foreignKeys = [
    ForeignKey(
      entity = Sunnah::class,
      parentColumns = ["id"],
      childColumns = ["sunnahId"]
    )
  ],
  indices = [Index("sunnahId")]
)
data class Interaction(
  val sunnahId: Int,
  val isTried: Boolean = false,
  val isPassed: Boolean = false,
  val tries: Int = 0,
  val passes: Int = 0,
  val isFavorite: Boolean = false,
  @PrimaryKey(autoGenerate = true)
  val interactionId: Int = 0
)
