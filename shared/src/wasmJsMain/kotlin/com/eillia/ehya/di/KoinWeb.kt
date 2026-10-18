/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.eillia.ehya.BuildInfo
import com.eillia.ehya.data.db.DATABASE_NAME
import com.eillia.ehya.data.db.EhyaDatabase
import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.Platform
import com.eillia.ehya.platform.ShareService
import com.eillia.ehya.platform.WebShareService
import com.eillia.ehya.platform.createSqliteWorker
import org.koin.core.module.Module
import org.koin.dsl.module

private val webModule: Module =
  module {
    // SQLite runs in a Web Worker (see `shared/webWorker/worker.js`) and persists to OPFS.
    single<RoomDatabase.Builder<EhyaDatabase>> {
      Room
        .databaseBuilder<EhyaDatabase>(name = DATABASE_NAME)
        .setDriver(WebWorkerSQLiteDriver(createSqliteWorker()))
    }

    single<ShareService> { WebShareService() }

    single { AppInfo(versionName = BuildInfo.VERSION_NAME, platform = Platform.WEB) }
  }

/** Called once from the web application's `main()`. */
fun initKoin() {
  startEhyaKoin(webModule)
}
