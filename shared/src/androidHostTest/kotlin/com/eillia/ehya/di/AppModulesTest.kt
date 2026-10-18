/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import androidx.room3.RoomDatabase
import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.ShareService
import kotlin.test.Test
import org.koin.dsl.module
import org.koin.test.verify.verify

/**
 * Koin resolves the graph at runtime, so this test stands in for the compile-time check a
 * code-generating DI library would give: every constructor parameter of every definition must be
 * provided by the shared modules or by one of the types each platform module contributes.
 */
class AppModulesTest {
  @Test
  fun sharedModulesResolveEveryDependency() {
    module { includes(dataModule, viewModelModule) }.verify(
      extraTypes = listOf(RoomDatabase.Builder::class, ShareService::class, AppInfo::class)
    )
  }
}
