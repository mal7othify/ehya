/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.play

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.passit
import com.eillia.ehya.resources.playagain
import com.eillia.ehya.resources.round_finished
import com.eillia.ehya.resources.tryit
import com.eillia.ehya.ui.components.DraggableCard
import com.eillia.ehya.ui.components.LoadingLogo
import com.eillia.ehya.ui.components.PulsingCircles
import com.eillia.ehya.ui.components.SunnahContent
import com.eillia.ehya.ui.components.contentEnter
import com.eillia.ehya.ui.components.loadingExit
import com.eillia.ehya.ui.components.rememberLaunchHold
import com.eillia.ehya.ui.theme.BlackOlive
import com.eillia.ehya.ui.theme.CardGradient
import com.eillia.ehya.ui.theme.Dimens
import com.eillia.ehya.ui.theme.PassRed
import com.eillia.ehya.ui.theme.PlayCardShape
import com.eillia.ehya.ui.theme.TryGreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val VISIBLE_CARDS = 3

@Composable
fun PlayScreen(modifier: Modifier = Modifier, viewModel: PlayViewModel = koinViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val holding = rememberLaunchHold()
  val shownState = if (holding) PlayUiState.Loading else uiState
  Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    AnimatedContent(
      targetState = shownState,
      transitionSpec = {
        if (initialState is PlayUiState.Loading) {
          contentEnter() togetherWith loadingExit()
        } else {
          fadeIn() togetherWith fadeOut()
        }
      },
      contentKey = { it::class },
      label = "play"
    ) { state ->
      when (state) {
        PlayUiState.Loading -> LoadingLogo()

        PlayUiState.RoundFinished -> RoundFinished(onPlayAgain = viewModel::playAgain)

        is PlayUiState.Playing -> CardStack(
          deck = state.deck,
          onSwiped = viewModel::onSwiped,
          onTry = viewModel::trySunnah,
          onPass = viewModel::passSunnah
        )
      }
    }
  }
}

/**
 * The deck. Cards behind the top one peek out above it, slightly narrower, like a fanned pile.
 * On phones the top card fills the width and sits flush with the bottom edge; on tablets,
 * desktops and wide browser windows it becomes a floating, fully rounded card of bounded size
 * centred in the window. When the window is short (a phone held sideways) the swipe actions
 * move beside the card so the hadith keeps its room.
 */
@Composable
private fun CardStack(
  deck: Deck,
  onSwiped: (SwipeDirection, Sunnah) -> Unit,
  onTry: (Sunnah) -> Unit,
  onPass: (Sunnah) -> Unit
) {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  val compactWidth = !sizeClass.isWidthAtLeastBreakpoint(
    WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
  )
  val compactHeight = !sizeClass.isHeightAtLeastBreakpoint(
    WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
  )
  val floating = !compactWidth && !compactHeight
  val actionsBeside = compactHeight && !compactWidth
  val shape = if (floating) MaterialTheme.shapes.extraLarge else PlayCardShape
  val sizeModifier = if (floating) {
    Modifier
      .widthIn(max = Dimens.MaxContentWidth)
      .fillMaxWidth()
      .heightIn(max = Dimens.MaxCardHeight)
      .fillMaxHeight()
      .padding(bottom = Dimens.PaddingNormal)
  } else {
    Modifier.fillMaxSize()
  }
  val top = deck.cards.firstOrNull()

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = if (floating) Alignment.Center else Alignment.BottomCenter
  ) {
    Row(modifier = sizeModifier, verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
          .padding(horizontal = Dimens.PaddingNormal)
          .padding(top = Dimens.PaddingNormal + Dimens.StackPeek * (VISIBLE_CARDS - 1)),
        contentAlignment = Alignment.TopCenter
      ) {
        // Draw from the bottom of the visible stack up, so the top card is composed last (on top).
        val visible = deck.cards.take(VISIBLE_CARDS)
        visible.indices.reversed().forEach { index ->
          val sunnah = visible[index]
          val isTop = index == 0
          // Keyed per card so drag state never leaks from a dismissed card to the next one.
          key(sunnah.id) {
            DraggableCard(
              enabled = isTop,
              shape = shape,
              onSwiped = { direction -> onSwiped(direction, sunnah) },
              modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                  // Each card behind the top one slides up and shrinks around its top edge.
                  translationY = -Dimens.StackPeek.toPx() * index
                  val shrink = 1f - index * 0.05f
                  scaleX = shrink
                  scaleY = shrink
                  transformOrigin = TransformOrigin(0.5f, 0f)
                }
            ) {
              when {
                // Paint the gradient over the whole card, not just behind the text, so nothing
                // dark shows through when the card above is dragged away.
                !isTop -> SunnahContent(
                  sunnah = sunnah,
                  scrollable = false,
                  modifier = Modifier.fillMaxSize()
                )

                actionsBeside -> SunnahContent(sunnah = sunnah, modifier = Modifier.fillMaxSize())

                else -> Column(Modifier.fillMaxSize().background(CardGradient)) {
                  SunnahContent(
                    sunnah = sunnah,
                    showBackground = false,
                    modifier = Modifier.weight(1f)
                  )
                  SwipeActions(onTry = { onTry(sunnah) }, onPass = { onPass(sunnah) })
                }
              }
            }
          }
        }
      }
      if (actionsBeside && top != null) {
        SwipeActions(
          onTry = { onTry(top) },
          onPass = { onPass(top) },
          vertical = true,
          modifier = Modifier.padding(end = Dimens.PaddingNormal)
        )
      }
    }
  }
}

/** Buttons for people who prefer tapping to swiping, and for accessibility services. */
@Composable
private fun SwipeActions(
  onTry: () -> Unit,
  onPass: () -> Unit,
  modifier: Modifier = Modifier,
  vertical: Boolean = false
) {
  // Beside the card the buttons sit on the dark window background, not on the light card.
  val labelColor = if (vertical) MaterialTheme.colorScheme.onBackground else BlackOlive
  val tryButton: @Composable () -> Unit = {
    ActionButton(
      label = stringResource(Res.string.tryit),
      labelColor = labelColor,
      icon = {
        Icon(
          Icons.Rounded.Check,
          contentDescription = null,
          tint = TryGreen,
          modifier = Modifier.size(40.dp)
        )
      },
      onClick = onTry
    )
  }
  val passButton: @Composable () -> Unit = {
    ActionButton(
      label = stringResource(Res.string.passit),
      labelColor = labelColor,
      icon = {
        Icon(
          Icons.Rounded.Close,
          contentDescription = null,
          tint = PassRed,
          modifier = Modifier.size(40.dp)
        )
      },
      onClick = onPass
    )
  }
  if (vertical) {
    Column(
      modifier = modifier,
      verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      tryButton()
      passButton()
    }
  } else {
    Row(
      modifier = modifier.fillMaxWidth().padding(vertical = Dimens.PaddingSmall),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      tryButton()
      passButton()
    }
  }
}

@Composable
private fun ActionButton(
  label: String,
  labelColor: Color,
  icon: @Composable () -> Unit,
  onClick: () -> Unit
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(contentAlignment = Alignment.Center) {
      PulsingCircles(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(84.dp))
      IconButton(onClick = onClick, modifier = Modifier.size(64.dp)) { icon() }
    }
    Text(label, style = MaterialTheme.typography.labelLarge, color = labelColor)
  }
}

@Composable
private fun RoundFinished(onPlayAgain: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize().padding(Dimens.PaddingLarge),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(Res.string.round_finished),
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.height(Dimens.PaddingLarge))
    Button(onClick = onPlayAgain) {
      Icon(Icons.Rounded.Refresh, contentDescription = null)
      Spacer(Modifier.size(8.dp))
      Text(stringResource(Res.string.playagain), color = Color.Unspecified)
    }
  }
}
