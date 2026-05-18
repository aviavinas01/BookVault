package com.example.bookvault.data.repository

import com.example.bookvault.data.local.SavedBookDao
import com.example.bookvault.data.local.toDomain
import com.example.bookvault.data.local.toSavedBookEntity
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.domain.repository.SavedBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedBookRepositoryImpl(
    private val dao: SavedBookDao
) : SavedBookRepository {

    override fun getSavedBooks(): Flow<List<SavedBook>> =
        dao.getAllSavedBooks().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun saveBook(book: Book): Result<Unit> {
        return try {
            dao.saveBook(book.toSavedBookEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSavedBook(id: Int): Result<Unit> {
        return try {
            dao.deleteSavedBook(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isBookSaved(id: Int): Boolean {
        return try {
            dao.isBookSaved(id)
        } catch (e: Exception) {
            false
        }
    }
}