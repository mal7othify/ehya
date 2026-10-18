/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.info

import androidx.lifecycle.ViewModel
import com.eillia.ehya.platform.AppInfo
import com.eillia.ehya.platform.PLAY_STORE_URL
import com.eillia.ehya.platform.ShareService

class InfoViewModel(val appInfo: AppInfo, private val shareService: ShareService) : ViewModel() {
  fun shareApp(message: (String) -> String) {
    shareService.share(message(appInfo.storeUrl ?: PLAY_STORE_URL))
  }
}
