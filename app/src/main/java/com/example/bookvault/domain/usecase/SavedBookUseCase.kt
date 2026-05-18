package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.repository.SavedBookRepository

class SaveBookUseCase(private val repository: SavedBookRepository) {
    suspend operator fun invoke(book: Book): Result<Unit> =
        repository.saveBook(book)
}