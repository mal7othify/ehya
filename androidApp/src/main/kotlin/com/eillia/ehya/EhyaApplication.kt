/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya

import android.app.Application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.eillia.ehya.di.initKoin

class EhyaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    Logger.setMinSeverity(if (BuildConfig.DEBUG) Severity.Verbose else Severity.Warn)
    initKoin(this)
  }
}
