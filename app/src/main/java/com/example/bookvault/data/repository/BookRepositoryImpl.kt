package com.example.bookvault.data.repository

import com.example.bookvault.data.local.BookDao
import com.example.bookvault.data.local.toDomain
import com.example.bookvault.data.local.toEntity
import com.example.bookvault.data.remote.BookApiService
import com.example.bookvault.data.remote.toDomain
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class BookRepositoryImpl(
    private val api: BookApiService,
    private val dao: BookDao
) : BookRepository {

    override suspend fun getBooks(query: String?): Result<List<Book>> {
        return try {
            // null/blank query → API defaults to popular-by-editions; otherwise pass through.
            val books = api.getAllBooks(query = query?.takeIf { it.isNotBlank() })
                .map { it.toDomain() }
            dao.insertBooks(books.map { it.toEntity() })
            Result.success(books)
        } catch (e: Exception) {
            val cached = dao.getAllBooks().map { it.toDomain() }
            if (cached.isNotEmpty()) Result.success(cached)
            else Result.failure(e)
        }
    }

    override suspend fun getBookById(id: Int): Result<Book> {
        val cached = dao.getBookById(id)?.toDomain()
        return if (cached != null) Result.success(cached)
        else Result.failure(NoSuchElementException("Book $id not in cache"))
    }

    override suspend fun addBook(book: Book): Result<Book> {
        return try {
            // Open Library is read-only — user-added books are kept locally only.
            // Negative IDs avoid collisions with Open Library's positive IDs.
            val newId = if (book.id == 0) {
                -(System.currentTimeMillis() and 0x7FFFFFFF).toInt()
            } else book.id
            val newBook = book.copy(id = newId)
            dao.insertBook(newBook.toEntity())
            Result.success(newBook)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBook(id: Int): Result<Unit> {
        return try {
            dao.deleteBook(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
