/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.platform

/** Facts about the running build that the UI shows on the info screen. */
data class AppInfo(
  val versionName: String,
  val platform: Platform,
  /** Where people can install the app from, if the platform has a store listing. */
  val storeUrl: String? = null
)

enum class Platform { ANDROID, IOS, WEB }

const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.eillia.ehya"
