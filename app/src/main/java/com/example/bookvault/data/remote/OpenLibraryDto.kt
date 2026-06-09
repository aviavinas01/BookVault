package com.example.bookvault.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibrarySearchResponse(
    val docs: List<OpenLibraryDoc> = emptyList()
)

@Serializable
data class OpenLibraryDoc(
    val key: String,
    val title: String,
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("cover_i") val coverI: Int? = null,
    @SerialName("number_of_pages_median") val numberOfPagesMedian: Int? = null,
    @SerialName("first_sentence") val firstSentence: List<String>? = null,
    val subject: List<String>? = null
)

private val OlKeyRegex = Regex("/works/OL(\\d+)[A-Z]")

fun OpenLibraryDoc.toBookDtoOrNull(): BookDto? {
    val numericId = OlKeyRegex.find(key)?.groupValues?.get(1)?.toIntOrNull() ?: return null
    if (title.isBlank()) return null

    val author = authorName?.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: "Unknown author"
    val genres = subject?.take(4)?.joinToString(" • ").orEmpty()
    val description = buildString {
        append("By ").append(author)
        if (genres.isNotEmpty()) {
            append("\n\n").append(genres)
        }
    }

    return BookDto(
        id = numericId,
        title = title,
        description = description,
        pageCount = numberOfPagesMedian ?: 0,
        excerpt = firstSentence?.firstOrNull().orEmpty(),
        publishDate = firstPublishYear?.toString(),
        coverUrl = coverI?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    )
}
