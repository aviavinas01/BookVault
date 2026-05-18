package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.BookRepository

class GetBookByIdUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Int): Result<Book> {
        return repository.getBookById(id)
    }
}