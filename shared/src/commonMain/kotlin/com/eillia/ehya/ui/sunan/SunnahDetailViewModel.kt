/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.sunan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.data.repository.SunanRepository
import com.eillia.ehya.platform.ShareService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SunnahDetailViewModel(
  val sunnahId: Int,
  repository: SunanRepository,
  private val shareService: ShareService
) : ViewModel() {
  val sunnah: StateFlow<Sunnah?> =
    repository
      .observeSunnah(
        sunnahId
      ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  fun share() {
    sunnah.value?.let { shareService.share(it.shareText) }
  }
}
