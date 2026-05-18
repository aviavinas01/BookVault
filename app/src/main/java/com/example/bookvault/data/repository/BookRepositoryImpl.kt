package com.example.bookvault.data.repository

import com.example.bookvault.data.local.BookDao
import com.example.bookvault.data.local.toDomain
import com.example.bookvault.data.local.toEntity
import com.example.bookvault.data.remote.BookApiService
import com.example.bookvault.data.remote.toDomain
import com.example.bookvault.data.remote.toDto
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class BookRepositoryImpl(
    private val api: BookApiService,
    private val dao: BookDao
) : BookRepository {

    override suspend fun getBooks(): Result<List<Book>> {
        return try {
            val books = api.getAllBooks().map { it.toDomain() }
            dao.insertBooks(books.map { it.toEntity() })
            Result.success(books)
        } catch (e: Exception) {
            val cached = dao.getAllBooks().map { it.toDomain() }
            if (cached.isNotEmpty()) Result.success(cached)
            else Result.failure(e)
        }
    }

    override suspend fun getBookById(id: Int): Result<Book> {
        return try {
            Result.success(api.getBookById(id).toDomain())
        } catch (e: Exception) {
            val cached = dao.getBookById(id)?.toDomain()
            if (cached != null) Result.success(cached)
            else Result.failure(e)
        }
    }

    override suspend fun addBook(book: Book): Result<Book> {
        return try {
            val result = api.addBook(book.toDto()).toDomain()
            dao.insertBook(result.toEntity())
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBook(id: Int): Result<Unit> {
        return try {
            api.deleteBook(id)
            dao.deleteBook(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}