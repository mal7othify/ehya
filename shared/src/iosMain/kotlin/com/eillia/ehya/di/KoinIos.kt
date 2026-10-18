/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.eillia.ehya.data.db.DATABASE_NAME
import com.eillia.ehya.data.db.EhyaDatabase
import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.IosShareService
import com.eillia.ehya.platform.Platform
import com.eillia.ehya.platform.ShareService
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal val iosModule: Module =
  module {
    single<RoomDatabase.Builder<EhyaDatabase>> {
      Room
        .databaseBuilder<EhyaDatabase>(name = documentDirectory() + "/" + DATABASE_NAME)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
    }

    single<ShareService> { IosShareService() }

    single {
      AppInfo(
        versionName =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
          ?: "",
        platform = Platform.IOS
      )
    }
  }

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
  val url =
    NSFileManager.defaultManager.URLForDirectory(
      directory = NSDocumentDirectory,
      inDomain = NSUserDomainMask,
      appropriateForURL = null,
      create = false,
      error = null
    )
  return requireNotNull(url?.path) { "Could not resolve the documents directory" }
}

private val koinApplication by lazy { startEhyaKoin(iosModule) }

/** Starts Koin the first time it is called; later calls are no-ops. Used by [com.eillia.ehya.MainViewController]. */
fun ensureKoinStarted() {
  koinApplication
}
