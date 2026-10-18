/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.ui.sunan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eillia.ehya.data.model.Category
import com.eillia.ehya.data.model.CategoryKey
import com.eillia.ehya.data.model.Sunnah
import com.eillia.ehya.resources.Res
import com.eillia.ehya.resources.all_categories
import com.eillia.ehya.resources.no_sunan
import com.eillia.ehya.resources.share_sunnah
import com.eillia.ehya.ui.theme.BlackOlive
import com.eillia.ehya.ui.theme.CardGradientHorizontal
import com.eillia.ehya.ui.theme.Dimens
import com.eillia.ehya.ui.theme.Mauve
import com.eillia.ehya.ui.theme.SunnahCardShape
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SunanScreen(
  onOpenSunnah: (Sunnah) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SunanViewModel = koinViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  SunanContent(
    uiState = uiState,
    onSelectCategory = viewModel::selectCategory,
    onOpenSunnah = { sunnah ->
      viewModel.onSunnahOpened(sunnah.id)
      onOpenSunnah(sunnah)
    },
    onShare = viewModel::share,
    modifier = modifier
  )
}

@Composable
fun SunanContent(
  uiState: SunanUiState,
  onSelectCategory: (CategoryKey?) -> Unit,
  onOpenSunnah: (Sunnah) -> Unit,
  onShare: (Sunnah) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier.fillMaxSize()) {
    CategoryFilters(
      categories = uiState.categories,
      selected = uiState.selectedCategory,
      onSelect = onSelectCategory
    )
    when {
      uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
      }

      uiState.visibleSunan.isEmpty() -> Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text(stringResource(Res.string.no_sunan), style = MaterialTheme.typography.bodyLarge)
      }

      else -> LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Dimens.SunnahTileMinWidth),
        contentPadding = PaddingValues(Dimens.PaddingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
        modifier = Modifier.fillMaxSize()
      ) {
        items(uiState.visibleSunan, key = { it.id }) { sunnah ->
          SunnahCard(
            sunnah = sunnah,
            selected = sunnah.id == uiState.selectedSunnahId,
            onClick = { onOpenSunnah(sunnah) },
            onShare = { onShare(sunnah) }
          )
        }
      }
    }
  }
}

@Composable
private fun CategoryFilters(
  categories: List<Category>,
  selected: CategoryKey?,
  onSelect: (CategoryKey?) -> Unit
) {
  if (categories.isEmpty()) return
  LazyRow(
    contentPadding = PaddingValues(
      horizontal = Dimens.PaddingNormal,
      vertical = Dimens.PaddingSmall
    ),
    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
  ) {
    item("all") {
      FilterChip(
        selected = selected == null,
        onClick = { onSelect(null) },
        label = { Text(stringResource(Res.string.all_categories)) }
      )
    }
    items(categories, key = { it.key }) { category ->
      FilterChip(
        selected = selected == category.key,
        onClick = { onSelect(category.key) },
        label = { Text(category.title) }
      )
    }
  }
}

@Composable
fun SunnahCard(
  sunnah: Sunnah,
  selected: Boolean,
  onClick: () -> Unit,
  onShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = SunnahCardShape,
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    modifier = modifier
      .fillMaxWidth()
      .then(
        if (selected) {
          Modifier.border(
            2.dp,
            MaterialTheme.colorScheme.primary,
            SunnahCardShape
          )
        } else {
          Modifier
        }
      )
      .clickable(onClick = onClick)
  ) {
    Column(
      modifier = Modifier
        .background(CardGradientHorizontal)
        .padding(Dimens.PaddingNormal),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = sunnah.title,
          style = MaterialTheme.typography.titleMedium,
          color = Mauve,
          modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onShare) {
          Icon(
            imageVector = Icons.Rounded.Share,
            contentDescription = stringResource(Res.string.share_sunnah),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
      sunnah.quantity?.takeIf { it.isNotBlank() }?.let { quantity ->
        Text(
          text = quantity,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = BlackOlive,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
      Text(
        text = sunnah.hadith,
        style = MaterialTheme.typography.bodyLarge,
        color = BlackOlive,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}
