/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.di

import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.Platform
import com.eillia.ehya.ui.info.InfoViewModel
import com.eillia.ehya.ui.play.PlayViewModel
import com.eillia.ehya.ui.sunan.SunanViewModel
import com.eillia.ehya.ui.sunan.SunnahDetailViewModel
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

/** Runs on the iOS simulator: the real iOS module must resolve every ViewModel at runtime. */
class IosKoinTest {
  @AfterTest
  fun tearDown() = stopKoin()

  @Test
  fun iosModuleResolvesEveryViewModel() {
    val koin = startEhyaKoin(iosModule).koin
    assertNotNull(koin.get<PlayViewModel>())
    assertNotNull(koin.get<SunanViewModel>())
    assertNotNull(koin.get<InfoViewModel>())
    assertEquals(7, koin.get<SunnahDetailViewModel> { parametersOf(7) }.sunnahId)
    assertEquals(Platform.IOS, koin.get<AppInfo>().platform)
  }
}
