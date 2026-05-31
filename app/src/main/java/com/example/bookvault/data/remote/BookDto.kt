package com.example.bookvault.data.remote

import com.example.bookvault.domain.model.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    val title: String,
    val description: String,
    val pageCount: Int,
    val excerpt: String,
    val publishDate: String? = null,
    val coverUrl: String? = null
)

fun BookDto.toDomain() = Book(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate.orEmpty(),
    coverUrl = coverUrl
)

fun Book.toDto() = BookDto(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate,
    coverUrl = coverUrl
)