package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class AddBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(book: Book): Result<Book> {
        return repository.addBook(book)
    }
}