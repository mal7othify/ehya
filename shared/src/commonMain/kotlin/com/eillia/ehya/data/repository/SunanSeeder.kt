/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.repository

import co.touchlab.kermit.Logger
import com.eillia.ehya.data.db.SunnahDao
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.resources.Res
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

/**
 * Fills the database from the bundled `sunan.json` the first time the app runs.
 * Replaces the WorkManager based seeding of the Android-only app so it works everywhere.
 */
class SunanSeeder(private val sunnahDao: SunnahDao, private val json: Json) {
  private val mutex = Mutex()
  private var seeded = false

  suspend fun seedIfEmpty() {
    if (seeded) return
    mutex.withLock {
      if (seeded) return
      if (sunnahDao.count() == 0) {
        val sunan = parse(Res.readBytes(SUNAN_FILE).decodeToString())
        sunnahDao.insertAll(sunan)
        Logger.d { "Seeded database with ${sunan.size} sunan" }
      }
      seeded = true
    }
  }

  /** Decodes the bundled list and assigns stable ids that follow the file order. */
  fun parse(rawJson: String): List<Sunnah> = json
    .decodeFromString<List<Sunnah>>(rawJson)
    .mapIndexed { index, sunnah -> sunnah.copy(id = index + 1) }

  companion object {
    const val SUNAN_FILE = "files/sunan.json"
  }
}
