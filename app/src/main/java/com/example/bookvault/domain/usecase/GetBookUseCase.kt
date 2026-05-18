package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class GetBooksUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(): Result<List<Book>> {
        return repository.getBooks()
    }
}