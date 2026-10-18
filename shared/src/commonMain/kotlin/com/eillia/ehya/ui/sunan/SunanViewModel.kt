/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.sunan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eillia.ehya.data.model.Category
import com.eillia.ehya.data.model.CategoryKey
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.data.repository.SunanRepository
import com.eillia.ehya.platform.ShareService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SunanUiState(
  val isLoading: Boolean = true,
  val sunan: List<Sunnah> = emptyList(),
  val categories: List<Category> = emptyList(),
  val selectedCategory: CategoryKey? = null,
  val selectedSunnahId: Int? = null
) {
  val visibleSunan: List<Sunnah>
    get() = selectedCategory?.let { key -> sunan.filter { it.category.key == key } } ?: sunan
}

class SunanViewModel(repository: SunanRepository, private val shareService: ShareService) :
  ViewModel() {
  private val selectedCategory = MutableStateFlow<CategoryKey?>(null)
  private val selectedSunnahId = MutableStateFlow<Int?>(null)

  val uiState: StateFlow<SunanUiState> =
    combine(
      repository.observeSunan(),
      selectedCategory,
      selectedSunnahId
    ) { sunan, category, selected ->
      SunanUiState(
        isLoading = false,
        sunan = sunan,
        categories = sunan.map { it.category }.distinctBy { it.key },
        selectedCategory = category,
        selectedSunnahId = selected
      )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SunanUiState())

  fun selectCategory(key: CategoryKey?) {
    selectedCategory.value = if (selectedCategory.value == key) null else key
  }

  /** Remembered so the list can highlight the item that is open in the detail pane. */
  fun onSunnahOpened(id: Int?) {
    selectedSunnahId.value = id
  }

  fun share(sunnah: Sunnah) = shareService.share(sunnah.shareText)
}
