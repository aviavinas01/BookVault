package com.example.bookvault.domain.model

data class SavedBook(
    val id: Int,
    val title: String,
    val description: String,
    val pageCount: Int,
    val excerpt: String,
    val publishDate: String,
    val coverUrl: String? = null,
    val savedAt: Long = System.currentTimeMillis()
)