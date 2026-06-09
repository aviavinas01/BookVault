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
    @SerialName("cover_edition_key") val coverEditionKey: String? = null,
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

    // Guaranteed non-null cover URL via a 3-tier fallback chain.
    // - cover_i: most reliable, points to an existing cover
    // - cover_edition_key: OL sometimes provides this when cover_i is absent
    // - work OLID: last-resort guess; ?default=false returns 404 instead of
    //   OL's ugly placeholder so Coil falls through to our procedural initials.
    val coverUrl = when {
        coverI != null ->
            "https://covers.openlibrary.org/b/id/$coverI-L.jpg"
        coverEditionKey != null ->
            "https://covers.openlibrary.org/b/olid/$coverEditionKey-L.jpg"
        else ->
            "https://covers.openlibrary.org/b/olid/OL${numericId}W-L.jpg?default=false"
    }

    return BookDto(
        id = numericId,
        title = title,
        description = description,
        pageCount = numberOfPagesMedian ?: 0,
        excerpt = firstSentence?.firstOrNull().orEmpty(),
        publishDate = firstPublishYear?.toString(),
        coverUrl = coverUrl
    )
}
