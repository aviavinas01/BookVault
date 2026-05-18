package com.example.bookvault.domain.repository

import com.example.bookvault.domain.model.Book

interface BookRepository {
    suspend fun getBooks(): Result<List<Book>>
    suspend fun getBookById(id: Int): Result<Book>
    suspend fun addBook(book: Book): Result<Book>
    suspend fun deleteBook(id: Int): Result<Unit>
}