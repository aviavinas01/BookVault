package com.example.bookvault.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedBookDao {

    @Query("SELECT * FROM saved_books ORDER BY savedAt DESC")
    fun getAllSavedBooks(): Flow<List<SavedBookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_books WHERE id = :id)")
    suspend fun isBookSaved(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: SavedBookEntity)

    @Query("DELETE FROM saved_books WHERE id = :id")
    suspend fun deleteSavedBook(id: Int)
}