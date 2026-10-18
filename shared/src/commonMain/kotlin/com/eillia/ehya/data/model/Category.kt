/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.model

import androidx.room3.ColumnInfo
import kotlinx.serialization.Serializable

/**
 * A category a [Sunnah] belongs to (prayer, food, sleep...). It is embedded in the
 * `sunnahTable` row and decoded straight from the bundled `sunan.json`.
 */
@Serializable
data class Category(
  @ColumnInfo(name = "category_title")
  val title: String,
  val imageId: String,
  val color: String? = null,
  @ColumnInfo(name = "category_id")
  val id: Int = 0
) {
  /** Stable identifier used for filtering and for picking the illustration. */
  val key: CategoryKey get() = CategoryKey.fromImageId(imageId)
}

enum class CategoryKey(val imageId: String) {
  WUDOO("wudoo"),
  PRAY("pray"),
  SLEEP("sleep"),
  FOOD("food"),
  APPEARANCE("appearance"),
  GENERAL("general")
  ;

  companion object {
    fun fromImageId(imageId: String): CategoryKey =
      entries.firstOrNull { it.imageId == imageId } ?: GENERAL
  }
}
