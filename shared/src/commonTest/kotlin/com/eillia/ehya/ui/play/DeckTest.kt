/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.play

import com.eillia.ehya.data.model.Category
import com.eillia.ehya.data.model.Sunnah
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeckTest {
  private val sunan = (1..5).map { sunnah(it) }

  @Test
  fun shuffledDeckKeepsEveryCard() {
    val deck = Deck.shuffled(sunan, Random(42))
    assertEquals(sunan.toSet(), deck.cards.toSet())
    assertEquals(sunan.size, deck.cards.size)
  }

  @Test
  fun triedCardLeavesTheDeck() {
    val deck = Deck(sunan)
    val after = deck.markTried(sunan[0])
    assertEquals(sunan.drop(1), after.cards)
  }

  @Test
  fun passedCardMovesToTheBottom() {
    val deck = Deck(sunan)
    val after = deck.markPassed(sunan[0])
    assertEquals(sunan.drop(1) + sunan[0], after.cards)
    assertEquals(sunan[1], after.top)
  }

  @Test
  fun passingUnknownCardIsANoOp() {
    val deck = Deck(sunan)
    assertEquals(deck, deck.markPassed(sunnah(99)))
  }

  @Test
  fun deckIsEmptyOnceEverythingWasTried() {
    var deck = Deck(sunan)
    sunan.forEach { deck = deck.markTried(it) }
    assertTrue(deck.isEmpty)
  }

  private fun sunnah(id: Int) = Sunnah(
    title = "سنة $id",
    quantity = null,
    category = Category(title = "عام", imageId = "general"),
    hadith = "حديث",
    strength = "صحيح",
    howto = "كيفية",
    id = id
  )
}
