package com.example.bookvault.domain.repository

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.model.SavedBook
import kotlinx.coroutines.flow.Flow

interface SavedBookRepository {
    fun getSavedBooks(): Flow<List<SavedBook>>
    suspend fun saveBook(book: Book): Result<Unit>
    suspend fun deleteSavedBook(id: Int): Result<Unit>
    suspend fun isBookSaved(id: Int): Boolean
}