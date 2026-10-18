/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.data.repository.SunanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PlayUiState {
  data object Loading : PlayUiState

  data class Playing(val deck: Deck) : PlayUiState

  data object RoundFinished : PlayUiState
}

enum class SwipeDirection { TRY, PASS }

class PlayViewModel(private val repository: SunanRepository) : ViewModel() {
  private val _uiState = MutableStateFlow<PlayUiState>(PlayUiState.Loading)
  val uiState: StateFlow<PlayUiState> = _uiState.asStateFlow()

  private var allSunan: List<Sunnah> = emptyList()

  init {
    viewModelScope.launch {
      allSunan = repository.observeSunan().first { it.isNotEmpty() }
      startRound()
    }
  }

  fun onSwiped(direction: SwipeDirection, sunnah: Sunnah) = when (direction) {
    SwipeDirection.TRY -> trySunnah(sunnah)
    SwipeDirection.PASS -> passSunnah(sunnah)
  }

  fun trySunnah(sunnah: Sunnah) {
    updateDeck { markTried(sunnah) }
    viewModelScope.launch { repository.recordTry(sunnah.id) }
    Logger.d { "Tried sunnah ${sunnah.id}" }
  }

  fun passSunnah(sunnah: Sunnah) {
    updateDeck { markPassed(sunnah) }
    viewModelScope.launch { repository.recordPass(sunnah.id) }
    Logger.d { "Passed sunnah ${sunnah.id}" }
  }

  fun playAgain() = startRound()

  private fun startRound() {
    _uiState.value = PlayUiState.Playing(Deck.shuffled(allSunan))
  }

  private fun updateDeck(transform: Deck.() -> Deck) {
    _uiState.update { state ->
      if (state !is PlayUiState.Playing) return@update state
      val deck = state.deck.transform()
      if (deck.isEmpty) PlayUiState.RoundFinished else PlayUiState.Playing(deck)
    }
  }
}
