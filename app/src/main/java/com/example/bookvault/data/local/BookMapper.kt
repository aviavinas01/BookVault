package com.example.bookvault.data.local

import com.example.bookvault.domain.model.Book

fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate,
    coverUrl = coverUrl
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate,
    coverUrl = coverUrl
)