/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

/**
 * Web Worker speaking the `androidx.sqlite.driver.web.WebWorkerSQLiteDriver` protocol.
 *
 * Every request is `{ id, data: { cmd, ...args } }` and is answered with either
 * `{ id, data }` or `{ id, error }`.
 *
 * Supported commands:
 *  - open    { fileName }                 -> { databaseId }
 *  - prepare { databaseId, sql }          -> { statementId, parameterCount, columnNames }
 *  - step    { statementId, bindings }    -> { rows, columnTypes }
 *  - close   { statementId? databaseId? } -> {}
 *
 * Storage: the database is persisted in the Origin Private File System through SQLite's
 * "opfs-sahpool" VFS, which does not need COOP/COEP headers. If OPFS is unavailable
 * (older browsers, private mode, ...) the worker falls back to an in-memory database.
 */
import sqlite3InitModule from '@sqlite.org/sqlite-wasm';

let sqlite3 = null;
let persistentDbClass = null;

const databases = new Map();
const statements = new Map();
let nextDatabaseId = 1;
let nextStatementId = 1;

function reply(id, data) {
  postMessage({ id, data });
}

function fail(id, error) {
  postMessage({ id, error: error instanceof Error ? error.message : String(error) });
}

function openDatabase(fileName) {
  if (persistentDbClass && fileName && fileName !== ':memory:') {
    const path = fileName.startsWith('/') ? fileName : '/' + fileName;
    return new persistentDbClass(path);
  }
  return new sqlite3.oo1.DB(':memory:');
}

function open(id, request) {
  try {
    const databaseId = nextDatabaseId++;
    databases.set(databaseId, openDatabase(request.fileName));
    reply(id, { databaseId });
  } catch (error) {
    fail(id, error);
  }
}

function prepare(id, request) {
  const database = databases.get(request.databaseId);
  if (!database) {
    fail(id, 'Invalid database ID: ' + request.databaseId);
    return;
  }
  try {
    const statement = database.prepare(request.sql);
    const statementId = nextStatementId++;
    statements.set(statementId, statement);
    const columnNames = [];
    for (let i = 0; i < statement.columnCount; i++) {
      columnNames.push(sqlite3.capi.sqlite3_column_name(statement, i));
    }
    reply(id, {
      statementId,
      parameterCount: sqlite3.capi.sqlite3_bind_parameter_count(statement),
      columnNames,
    });
  } catch (error) {
    fail(id, error);
  }
}

function step(id, request) {
  const statement = statements.get(request.statementId);
  if (!statement) {
    fail(id, 'Invalid statement ID: ' + request.statementId);
    return;
  }
  try {
    statement.reset();
    statement.clearBindings();
    const bindings = request.bindings ?? [];
    for (let i = 0; i < bindings.length; i++) {
      statement.bind(i + 1, bindings[i]);
    }
    const rows = [];
    const columnTypes = [];
    while (statement.step()) {
      if (columnTypes.length === 0) {
        for (let i = 0; i < statement.columnCount; i++) {
          columnTypes.push(sqlite3.capi.sqlite3_column_type(statement, i));
        }
      }
      rows.push(statement.get([]));
    }
    reply(id, { rows, columnTypes });
  } catch (error) {
    fail(id, error);
  }
}

function close(id, request) {
  try {
    if (request.statementId != null) {
      const statement = statements.get(request.statementId);
      if (!statement) {
        fail(id, 'Invalid statement ID: ' + request.statementId);
        return;
      }
      statement.finalize();
      statements.delete(request.statementId);
    }
    if (request.databaseId != null) {
      const database = databases.get(request.databaseId);
      if (!database) {
        fail(id, 'Invalid database ID: ' + request.databaseId);
        return;
      }
      database.close();
      databases.delete(request.databaseId);
    }
    reply(id, {});
  } catch (error) {
    fail(id, error);
  }
}

const handlers = { open, prepare, step, close };

function handle(event) {
  const message = event.data;
  if (message == null || message.data == null) {
    fail(message?.id, "Invalid request, missing 'data'.");
    return;
  }
  const handler = handlers[message.data.cmd];
  if (!handler) {
    fail(message.id, "Invalid request, unknown command: '" + message.data.cmd + "'.");
    return;
  }
  handler(message.id, message.data);
}

const pending = [];
onmessage = (event) => {
  if (sqlite3) {
    handle(event);
  } else {
    pending.push(event);
  }
};

async function init() {
  const instance = await sqlite3InitModule({ print: () => {}, printErr: console.error });
  try {
    const pool = await instance.installOpfsSAHPoolVfs({ name: 'ehya-opfs' });
    persistentDbClass = pool.OpfsSAHPoolDb;
  } catch (error) {
    console.warn('OPFS is not available, falling back to an in-memory database.', error);
  }
  sqlite3 = instance;
  while (pending.length > 0) {
    handle(pending.shift());
  }
}

init();
