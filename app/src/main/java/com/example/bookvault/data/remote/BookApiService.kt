package com.example.bookvault.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class BookApiService(private val client: HttpClient) {

    private val baseUrl = "https://openlibrary.org"

    suspend fun getAllBooks(query: String? = null, limit: Int = 40): List<BookDto> {
        val response: OpenLibrarySearchResponse = client.get("$baseUrl/search.json") {
            // "bestseller" is a real text-search term that returns within ~1s.
            // Wildcard + sort=editions hangs OL's backend — never use that.
            parameter("q", query?.takeIf { it.isNotBlank() } ?: "bestseller")
            parameter("limit", limit)
            parameter(
                "fields",
                "key,title,author_name,first_publish_year,cover_i,cover_edition_key,number_of_pages_median,first_sentence,subject"
            )
        }.body()
        return response.docs.mapNotNull { it.toBookDtoOrNull() }
    }
}
