package com.example.bookvault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class SavedBookEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val pageCount: Int,
    val excerpt: String,
    val publishDate: String,
    val savedAt: Long = System.currentTimeMillis()
)