package com.example.bookvault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BookEntity::class,
        SavedBookEntity::class      // add this
    ],
    version = 2,                    // bump version from 1 to 2
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun savedBookDao(): SavedBookDao   // add this
}