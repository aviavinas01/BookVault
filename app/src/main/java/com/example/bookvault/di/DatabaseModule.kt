package com.example.bookvault.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookvault.data.local.BookDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS saved_books (
                id INTEGER NOT NULL PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                pageCount INTEGER NOT NULL,
                excerpt TEXT NOT NULL,
                publishDate TEXT NOT NULL,
                savedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BookDatabase::class.java,
            "book_vault_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single { get<BookDatabase>().bookDao() }
    single { get<BookDatabase>().savedBookDao() }
}