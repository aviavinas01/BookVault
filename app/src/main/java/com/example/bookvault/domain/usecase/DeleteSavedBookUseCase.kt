package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.repository.SavedBookRepository

class DeleteSavedBookUseCase(private val repository: SavedBookRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> =
        repository.deleteSavedBook(id)
}