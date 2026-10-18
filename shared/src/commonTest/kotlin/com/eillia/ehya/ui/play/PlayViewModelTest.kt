/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.play

import com.eillia.ehya.data.FakeSunanRepository
import com.eillia.ehya.data.model.Category
import com.eillia.ehya.data.model.Sunnah
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class PlayViewModelTest {
  private val dispatcher = StandardTestDispatcher()
  private val sunan = (1..3).map { sunnah(it) }

  @BeforeTest
  fun setUp() = Dispatchers.setMain(dispatcher)

  @AfterTest
  fun tearDown() = Dispatchers.resetMain()

  @Test
  fun startsLoadingThenDealsEveryCard() = runTest(dispatcher) {
    val viewModel = PlayViewModel(FakeSunanRepository(sunan))
    assertEquals(PlayUiState.Loading, viewModel.uiState.value)
    dispatcher.scheduler.advanceUntilIdle()
    val state = assertIs<PlayUiState.Playing>(viewModel.uiState.value)
    assertEquals(sunan.toSet(), state.deck.cards.toSet())
  }

  @Test
  fun tryingRemovesTheCardAndRecordsIt() = runTest(dispatcher) {
    val repository = FakeSunanRepository(sunan)
    val viewModel = PlayViewModel(repository)
    dispatcher.scheduler.advanceUntilIdle()
    val top = (viewModel.uiState.value as PlayUiState.Playing).deck.top!!

    viewModel.onSwiped(SwipeDirection.TRY, top)
    dispatcher.scheduler.advanceUntilIdle()

    val state = assertIs<PlayUiState.Playing>(viewModel.uiState.value)
    assertEquals(sunan.size - 1, state.deck.cards.size)
    assertEquals(listOf(top.id), repository.tries)
  }

  @Test
  fun passingKeepsTheCardAndRecordsIt() = runTest(dispatcher) {
    val repository = FakeSunanRepository(sunan)
    val viewModel = PlayViewModel(repository)
    dispatcher.scheduler.advanceUntilIdle()
    val top = (viewModel.uiState.value as PlayUiState.Playing).deck.top!!

    viewModel.onSwiped(SwipeDirection.PASS, top)
    dispatcher.scheduler.advanceUntilIdle()

    val state = assertIs<PlayUiState.Playing>(viewModel.uiState.value)
    assertEquals(sunan.size, state.deck.cards.size)
    assertEquals(top, state.deck.cards.last())
    assertEquals(listOf(top.id), repository.passes)
  }

  @Test
  fun roundFinishesAndCanBeReplayed() = runTest(dispatcher) {
    val viewModel = PlayViewModel(FakeSunanRepository(sunan))
    dispatcher.scheduler.advanceUntilIdle()
    sunan.forEach { viewModel.trySunnah(it) }
    dispatcher.scheduler.advanceUntilIdle()
    assertEquals(PlayUiState.RoundFinished, viewModel.uiState.value)

    viewModel.playAgain()
    assertIs<PlayUiState.Playing>(viewModel.uiState.value)
  }

  private fun sunnah(id: Int) = Sunnah(
    title = "سنة $id",
    category = Category(title = "عام", imageId = "general"),
    hadith = "حديث",
    strength = "صحيح",
    howto = "كيفية",
    id = id
  )
}
