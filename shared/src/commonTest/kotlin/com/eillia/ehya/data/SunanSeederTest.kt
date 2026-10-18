/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data

import com.eillia.ehya.data.db.SunnahDao
import com.eillia.ehya.data.model.CategoryKey
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.data.repository.SunanSeeder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class SunanSeederTest {
  private val rawJson = """
    [
      {
        "title": "المضمضة والاستنشاق بغَرفة واحدة",
        "quantity": "3 مرات",
        "category": { "title": "الوضوء", "imageId": "wudoo", "color": "" },
        "hadith": "حديث",
        "strength": "صحيح مسلم",
        "howto": "الكيفية"
      },
      {
        "title": "سنة ثانية",
        "quantity": "",
        "category": { "title": "عام", "imageId": "general", "color": "" },
        "hadith": "حديث آخر",
        "strength": "صحيح",
        "howto": "الكيفية",
        "unknownField": true
      }
    ]
  """

  private val json = Json { ignoreUnknownKeys = true }

  @Test
  fun parsesBundledFormatAndAssignsStableIds() {
    val seeder = SunanSeeder(InMemorySunnahDao(), json)
    val sunan = seeder.parse(rawJson)
    assertEquals(listOf(1, 2), sunan.map { it.id })
    assertEquals(CategoryKey.WUDOO, sunan[0].category.key)
    assertEquals(CategoryKey.GENERAL, sunan[1].category.key)
    assertEquals("3 مرات", sunan[0].quantity)
  }

  @Test
  fun unknownCategoryFallsBackToGeneral() {
    assertEquals(CategoryKey.GENERAL, CategoryKey.fromImageId("something-new"))
  }

  @Test
  fun seederOnlySeedsAnEmptyDatabase() = runTest {
    val dao = InMemorySunnahDao()
    dao.insertAll(SunanSeeder(dao, json).parse(rawJson))
    val seeder = SunanSeeder(dao, json)
    seeder.seedIfEmpty()
    assertEquals(1, dao.insertCalls)
  }

  private class InMemorySunnahDao : SunnahDao {
    private val rows = MutableStateFlow<List<Sunnah>>(emptyList())
    var insertCalls = 0

    override suspend fun insertAll(sunan: List<Sunnah>) {
      insertCalls++
      rows.value =
        (rows.value.filterNot { row -> sunan.any { it.id == row.id } } + sunan).sortedBy { it.id }
    }

    override fun observeAll(): Flow<List<Sunnah>> = rows

    override fun observeById(id: Int): Flow<Sunnah?> = rows.map { list ->
      list.firstOrNull {
        it.id ==
          id
      }
    }

    override suspend fun count(): Int = rows.value.size
  }
}
