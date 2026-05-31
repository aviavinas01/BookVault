package com.example.bookvault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class, SavedBookEntity::class],
    version = 3,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun savedBookDao(): SavedBookDao
}