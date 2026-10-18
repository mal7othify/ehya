/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.data.db

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.async.executeSQL
import com.eillia.ehya.data.model.Interaction
import com.eillia.ehya.data.model.Sunnah

const val DATABASE_NAME = "ehya-db"

@Database(
  entities = [Sunnah::class, Interaction::class],
  version = 4,
  exportSchema = true
)
@ConstructedBy(EhyaDatabaseConstructor::class)
abstract class EhyaDatabase : RoomDatabase() {
  abstract fun sunnahDao(): SunnahDao

  abstract fun interactionDao(): InteractionDao
}

/** The Room compiler generates the platform `actual` implementations. */
@Suppress("KotlinNoActualForExpect")
expect object EhyaDatabaseConstructor : RoomDatabaseConstructor<EhyaDatabase> {
  override fun initialize(): EhyaDatabase
}

/**
 * Version 3 (the last Android-only release) carried an unused `categoryTable`.
 * Everything else is unchanged, so user data survives the move to Room 3.
 */
object DropCategoryTableMigration : Migration(3, 4) {
  override suspend fun migrate(connection: SQLiteConnection) {
    connection.executeSQL("DROP TABLE IF EXISTS `categoryTable`")
  }
}

/** Shared configuration applied on every platform on top of the platform specific builder. */
fun RoomDatabase.Builder<EhyaDatabase>.configure(): RoomDatabase.Builder<EhyaDatabase> =
  addMigrations(DropCategoryTableMigration)
    .fallbackToDestructiveMigration(dropAllTables = true)
