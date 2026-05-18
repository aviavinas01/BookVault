package com.example.bookvault.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    val title: String,
    val description: String,
    val pageCount: Int,
    val excerpt: String,
    val publishDate: String
)

// Maps API response → Domain model
fun BookDto.toDomain() = com.example.bookvault.domain.model.Book(
    id = id,
    title = title,
    description = description,
    pageCount = pageCount,
    excerpt = excerpt,
    publishDate = publishDate
)