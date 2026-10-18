/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.platform

import org.w3c.dom.Worker

/**
 * Spawns the SQLite worker bundled from the local `ehya-sqlite-worker` npm package.
 * `import.meta.url` lets webpack emit the worker (and the SQLite WASM it imports) as its own chunk.
 */
fun createSqliteWorker(): Worker = newSqliteWorker()

private fun newSqliteWorker(): Worker =
  js("""new Worker(new URL("ehya-sqlite-worker/worker.js", import.meta.url))""")
