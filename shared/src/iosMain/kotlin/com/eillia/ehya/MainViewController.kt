/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya

import androidx.compose.ui.window.ComposeUIViewController
import com.eillia.ehya.di.ensureKoinStarted
import platform.UIKit.UIViewController

/** Called from SwiftUI (`ContentView.swift`) to host the shared Compose UI. */
@Suppress("FunctionName")
fun MainViewController(): UIViewController {
  ensureKoinStarted()
  return ComposeUIViewController { EhyaApp() }
}
