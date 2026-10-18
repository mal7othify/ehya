/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.platform

/** Uses the Web Share API where the browser offers it, otherwise copies the text to the clipboard. */
class WebShareService : ShareService {
  override fun share(text: String) {
    if (canUseWebShare()) shareWithWebShare(text) else copyToClipboard(text)
  }
}

private fun canUseWebShare(): Boolean =
  js("typeof navigator !== 'undefined' && typeof navigator.share === 'function'")

private fun shareWithWebShare(text: String) {
  js("navigator.share({ text: text }).catch(function () {})")
}

private fun copyToClipboard(text: String) {
  js("navigator.clipboard && navigator.clipboard.writeText(text)")
}
