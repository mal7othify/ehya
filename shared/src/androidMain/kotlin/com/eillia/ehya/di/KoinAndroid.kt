/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.eillia.ehya.data.db.DATABASE_NAME
import com.eillia.ehya.data.db.EhyaDatabase
import com.eillia.ehya.platform.AndroidShareService
import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.PLAY_STORE_URL
import com.eillia.ehya.platform.Platform
import com.eillia.ehya.platform.ShareService
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

private fun androidModule(context: Context): Module = module {
  single<Context> { context }

  single<RoomDatabase.Builder<EhyaDatabase>> {
    Room
      .databaseBuilder<EhyaDatabase>(
        context = get(),
        name = get<Context>().getDatabasePath(DATABASE_NAME).absolutePath
      ).setDriver(BundledSQLiteDriver())
      .setQueryCoroutineContext(Dispatchers.IO)
  }

  single<ShareService> { AndroidShareService(get()) }

  single {
    val appContext = get<Context>()
    val versionName =
      appContext.packageManager
        .getPackageInfo(
          appContext.packageName,
          0
        ).versionName
    AppInfo(
      versionName = versionName ?: "",
      platform = Platform.ANDROID,
      storeUrl = PLAY_STORE_URL
    )
  }
}

/** Called once from the Android `Application`. The container lives as long as the process. */
fun initKoin(context: Context) {
  startEhyaKoin(androidModule(context.applicationContext))
}
