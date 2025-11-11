package com.example.lab_week_10.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Create a database with the @Database annotation
// It has 2 parameters:
// entities: You can define which entities the database relies on.
// It can rely on multiple entities
// version: Used to define schema version when there's a change to the
//schema.
// Update the version when you try to change the schema
@Database(entities = [Total::class], version = 2)
abstract class TotalDatabase : RoomDatabase() {
    abstract fun totalDao(): TotalDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create a temporary table with the new schema (new columns)
                db.execSQL(
                    "CREATE TABLE total_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, value INTEGER, date TEXT)"
                )

                // Copy data from old table to the new table
                db.execSQL(
                    "INSERT INTO total_new (id, value, date) SELECT id, total.value, total.date FROM total"
                )

                // Drop the old table
                db.execSQL("DROP TABLE total")

                // Rename the new table to the original table name
                db.execSQL("ALTER TABLE total_new RENAME TO total")
            }
        }
    }
}