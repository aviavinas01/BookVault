package com.example.bookvault.domain.usecase

import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.domain.repository.SavedBookRepository
import kotlinx.coroutines.flow.Flow

class GetSavedBooksUseCase(private val repository: SavedBookRepository) {
    operator fun invoke(): Flow<List<SavedBook>> =
        repository.getSavedBooks()
}