/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.model

import androidx.room3.ColumnInfo
import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * A single sunnah. The table layout is kept identical to the original Android app so that
 * existing installs migrate without losing their interaction history.
 */
@Serializable
@Entity(tableName = "sunnahTable")
data class Sunnah(
  @ColumnInfo(name = "sunnah_title")
  val title: String,
  val quantity: String? = null,
  @Embedded
  val category: Category,
  val hadith: String,
  val strength: String,
  val howto: String,
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0
) {
  /** Text used when sharing this sunnah with someone else. */
  val shareText: String get() = "جرّب السنة المهجورة التالية: $title | $hadith"
}
