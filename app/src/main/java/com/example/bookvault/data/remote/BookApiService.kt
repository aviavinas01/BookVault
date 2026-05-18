package com.example.bookvault.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class BookApiService(private val client: HttpClient) {

    private val baseUrl = "https://fakerestapi.azurewebsites.net/api/v1/Books"

    suspend fun getAllBooks(): List<BookDto> =
        client.get(baseUrl).body()

    suspend fun getBookById(id: Int): BookDto =
        client.get("$baseUrl/$id").body()

    suspend fun addBook(book: BookDto): BookDto =
        client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(book)
        }.body()

    suspend fun deleteBook(id: Int) {
        client.delete("$baseUrl/$id")
    }
}