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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Generates a unique but consistent gradient per book title
fun generateBookColors(title: String): Pair<Color, Color> {
    val gradients = listOf(
        Pair(Color(0xFF2C3E50), Color(0xFF3498DB)),
        Pair(Color(0xFF1A1A2E), Color(0xFF16213E)),
        Pair(Color(0xFF2D1B69), Color(0xFF11998E)),
        Pair(Color(0xFF373B44), Color(0xFF4286F4)),
        Pair(Color(0xFF0F2027), Color(0xFF2C5364)),
        Pair(Color(0xFF1F4037), Color(0xFF99F2C8)),
        Pair(Color(0xFF3A1C71), Color(0xFFD76D77)),
        Pair(Color(0xFF4A00E0), Color(0xFF8E2DE2)),
        Pair(Color(0xFF134E5E), Color(0xFF71B280)),
        Pair(Color(0xFF360033), Color(0xFF0B8793)),
        Pair(Color(0xFF1D4350), Color(0xFFA43931)),
        Pair(Color(0xFF0052D4), Color(0xFF4364F7)),
    )
    val index = kotlin.math.abs(title.hashCode()) % gradients.size
    return gradients[index]
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
    cornerRadius: Dp = 10.dp
) {
    val (startColor, endColor) = generateBookColors(title)
    val initials = getInitials(title)

    Box(
        modifier = Modifier
            .size(width = size * 0.7f, height = size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = (size.value * 0.28f).sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LargeBookCover(title: String) {
    val (startColor, endColor) = generateBookColors(title)
    val initials = getInitials(title)

    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            ),
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
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}