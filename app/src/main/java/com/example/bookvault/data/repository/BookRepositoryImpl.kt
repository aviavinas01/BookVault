package com.example.bookvault.data.repository

import com.example.bookvault.data.local.BookDao
import com.example.bookvault.data.local.toDomain
import com.example.bookvault.data.local.toEntity
import com.example.bookvault.data.remote.BookApiService
import com.example.bookvault.data.remote.BookDto
import com.example.bookvault.data.remote.toDomain
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class BookRepositoryImpl(
    private val api: BookApiService,
    private val dao: BookDao
) : BookRepository {

    override suspend fun getBooks(): Result<List<Book>> {
        return try {
            val remote = api.getAllBooks().map { it.toDomain() }
            dao.insertBooks(remote.map { it.toEntity() })
            Result.success(remote)
        } catch (e: Exception) {
            android.util.Log.e("BookRepo", "API failed: ${e.message}", e)
            val local = dao.getAllBooks().map { it.toDomain() }
            if (local.isNotEmpty()) Result.success(local)
            else Result.failure(e)
        }
    }

    override suspend fun getBookById(id: Int): Result<Book> {
        return try {
            Result.success(api.getBookById(id).toDomain())
        } catch (e: Exception) {
            val local = dao.getBookById(id)?.toDomain()
            if (local != null) Result.success(local)
            else Result.failure(e)
        }
    }

    override suspend fun addBook(book: Book): Result<Book> {
        return try {
            val dto = BookDto(
                id = book.id,
                title = book.title,
                description = book.description,
                pageCount = book.pageCount,
                excerpt = book.excerpt,
                publishDate = book.publishDate
            )
            val result = api.addBook(dto).toDomain()
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