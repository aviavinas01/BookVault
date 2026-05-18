package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.repository.SavedBookRepository

class IsBookSavedUseCase(private val repository: SavedBookRepository) {
    suspend operator fun invoke(id: Int): Boolean =
        repository.isBookSaved(id)
}