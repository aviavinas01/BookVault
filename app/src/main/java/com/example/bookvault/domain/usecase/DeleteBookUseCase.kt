package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.repository.BookRepository

class DeleteBookUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteBook(id)
    }
}