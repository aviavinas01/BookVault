package com.example.bookvault.data.local

import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.model.SavedBook

fun SavedBookEntity.toDomain(): SavedBook = SavedBook(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate,
    savedAt = savedAt
)

fun SavedBook.toEntity(): SavedBookEntity = SavedBookEntity(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate,
    savedAt = savedAt
)

fun Book.toSavedBookEntity(): SavedBookEntity = SavedBookEntity(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate
)