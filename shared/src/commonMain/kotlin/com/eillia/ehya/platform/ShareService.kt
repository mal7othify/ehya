/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.platform

/** Hands a piece of text to the platform share sheet (or the closest equivalent on the web). */
fun interface ShareService {
  fun share(text: String)
}
