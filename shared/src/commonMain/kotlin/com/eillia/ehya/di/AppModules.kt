/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import androidx.room3.RoomDatabase
import com.eillia.ehya.data.db.EhyaDatabase
import com.eillia.ehya.data.db.configure
import com.eillia.ehya.data.repository.DefaultSunanRepository
import com.eillia.ehya.data.repository.SunanRepository
import com.eillia.ehya.data.repository.SunanSeeder
import com.eillia.ehya.ui.info.InfoViewModel
import com.eillia.ehya.ui.play.PlayViewModel
import com.eillia.ehya.ui.sunan.SunanViewModel
import com.eillia.ehya.ui.sunan.SunnahDetailViewModel
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Bindings that are identical on every platform. Each platform contributes one more module with
 * a configured [RoomDatabase.Builder], a [com.eillia.ehya.platform.ShareService] and an
 * [com.eillia.ehya.platform.AppInfo], then calls [startEhyaKoin].
 */
val dataModule: Module =
  module {
    single { Json { ignoreUnknownKeys = true } }
    single { get<RoomDatabase.Builder<EhyaDatabase>>().configure().build() }
    factory { get<EhyaDatabase>().sunnahDao() }
    factory { get<EhyaDatabase>().interactionDao() }
    singleOf(::SunanSeeder)
    singleOf(::DefaultSunanRepository) bind SunanRepository::class
  }

val viewModelModule: Module =
  module {
    viewModelOf(::PlayViewModel)
    viewModelOf(::SunanViewModel)
    viewModelOf(::InfoViewModel)
    viewModel { (sunnahId: Int) -> SunnahDetailViewModel(sunnahId, get(), get()) }
  }

/** Starts the global Koin container with the shared modules plus the given platform module. */
internal fun startEhyaKoin(platformModule: Module): KoinApplication = startKoin {
  modules(platformModule, dataModule, viewModelModule)
}
