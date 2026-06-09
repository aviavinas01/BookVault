package com.example.bookvault.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Matte flat colour palette — no gradients, no gloss
private val mattePalette = listOf(
    Color(0xFF14213D), // Navy
    Color(0xFF1D2D44), // Dark Navy
    Color(0xFF4E342E), // Dark Brown
    Color(0xFF1B4332), // Forest Green
    Color(0xFF1A237E), // Deep Indigo
    Color(0xFF4A148C), // Deep Plum
    Color(0xFF37474F), // Blue Grey
    Color(0xFF263238), // Dark Teal
)

fun generateBookColor(title: String): Color {
    val index = kotlin.math.abs(title.hashCode()) % mattePalette.size
    return mattePalette[index]
}

// Keep old signature alias so existing call sites don't break
fun generateBookColors(title: String): Pair<Color, Color> {
    val c = generateBookColor(title)
    return Pair(c, c)
}

fun getInitials(title: String): String {
    val words = title.trim().split(" ").filter { it.isNotBlank() }
    return when {
        words.isEmpty() -> "?"
        words.size == 1 -> words[0].take(2).uppercase()
        else -> "${words[0].first()}${words[1].first()}".uppercase()
    }
}

@Composable
fun BookCoverPlaceholder(
    title: String,
    size: Dp = 56.dp,
    cornerRadius: Dp = 10.dp,
    coverUrl: String? = null
) {
    val bgColor = generateBookColor(title)
    val initials = getInitials(title)

    Box(
        modifier = Modifier
            .size(width = size * 0.7f, height = size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = (size.value * 0.28f).sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (!coverUrl.isNullOrBlank()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LargeBookCover(title: String, coverUrl: String? = null) {
    val bgColor = generateBookColor(title)
    val initials = getInitials(title)

    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (!coverUrl.isNullOrBlank()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}