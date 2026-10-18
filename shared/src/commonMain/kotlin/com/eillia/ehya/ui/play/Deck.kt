/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.play

import com.eillia.ehya.data.model.Sunnah
import kotlin.random.Random

/**
 * The stack of cards shown on the play screen. The first element is the card on top.
 * Pure and immutable so the rules are trivial to unit test.
 */
data class Deck(val cards: List<Sunnah>) {
  val top: Sunnah? get() = cards.firstOrNull()
  val isEmpty: Boolean get() = cards.isEmpty()

  /** A tried sunnah leaves the deck for this round. */
  fun markTried(sunnah: Sunnah): Deck = Deck(cards.filterNot { it.id == sunnah.id })

  /** A passed sunnah goes to the bottom so it shows up again later in the round. */
  fun markPassed(sunnah: Sunnah): Deck {
    if (cards.none { it.id == sunnah.id }) return this
    return Deck(cards.filterNot { it.id == sunnah.id } + sunnah)
  }

  companion object {
    fun shuffled(sunan: List<Sunnah>, random: Random = Random.Default): Deck =
      Deck(sunan.shuffled(random))
  }
}
