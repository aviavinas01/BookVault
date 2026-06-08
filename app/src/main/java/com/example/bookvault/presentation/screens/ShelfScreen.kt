package com.example.bookvault.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookvault.R
import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.presentation.ui.theme.PlayfairDisplay
import com.example.bookvault.presentation.viewmodel.BookViewModel
import kotlin.math.abs

private data class SpineStyle(val main: Color, val accent: Color, val text: Color)

private val SpinePalette = listOf(
    SpineStyle(Color(0xFF2E4A35), Color(0xFFE8A33D), Color.White),
    SpineStyle(Color(0xFF800020), Color(0xFFD4A574), Color.White),
    SpineStyle(Color(0xFF1A3D5C), Color(0xFFF2D5A8), Color.White),
    SpineStyle(Color(0xFFC85250), Color(0xFF3A2A1F), Color.White),
    SpineStyle(Color(0xFFE8A33D), Color(0xFF3A2A1F), Color(0xFF2C2C2C)),
    SpineStyle(Color(0xFFF2D5A8), Color(0xFF800020), Color(0xFF2C2C2C)),
    SpineStyle(Color(0xFF6B8E4E), Color(0xFFF2D5A8), Color.White),
    SpineStyle(Color(0xFF3A2A1F), Color(0xFFE8A33D), Color.White),
    SpineStyle(Color(0xFFD4A574), Color(0xFF3A2A1F), Color(0xFF2C2C2C)),
    SpineStyle(Color(0xFFB0413E), Color(0xFFE8B4B8), Color.White),
    SpineStyle(Color(0xFF2C2C2C), Color(0xFFD4A574), Color.White),
    SpineStyle(Color(0xFFF5F0E8), Color(0xFFB0413E), Color(0xFF2C2C2C)),
    SpineStyle(Color(0xFF7B9EA8), Color(0xFFF5F0E8), Color.White),
    SpineStyle(Color(0xFFE8B4B8), Color(0xFF800020), Color(0xFF2C2C2C)),
)

private val GenreArtwork = listOf(
    R.drawable.genre_fiction,
    R.drawable.genre_mystery,
    R.drawable.genre_fantasy,
    R.drawable.genre_classic,
    R.drawable.genre_poetry,
    R.drawable.genre_history,
    R.drawable.genre_science,
    R.drawable.genre_scifi,
    R.drawable.genre_drama,
)

private val ShelfBg = Color(0xFFFAF6F0)
private val ShelfWood = Color(0xFFC9A87A)
private val ShelfWoodGrain = Color(0xFF8D6940)
private val ShelfWoodEdge = Color(0xFF6B4F35)
private val InkBlack = Color(0xFF2C2C2C)
private const val MinShelves = 4
private val ShelfCapacities = listOf(6, 8, 5, 7, 4, 8, 6, 5)

private fun distributeBooks(books: List<SavedBook>): List<List<SavedBook>> {
    if (books.isEmpty()) return emptyList()
    val result = mutableListOf<List<SavedBook>>()
    var i = 0
    var rowIndex = 0
    while (i < books.size) {
        val capacity = ShelfCapacities[rowIndex % ShelfCapacities.size]
        val end = minOf(i + capacity, books.size)
        result.add(books.subList(i, end))
        i = end
        rowIndex++
    }
    return result
}

@Composable
fun ShelfScreen(
    viewModel: BookViewModel,
    onBookClick: (Int) -> Unit,
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val rows = remember(uiState.savedBooks) { distributeBooks(uiState.savedBooks) }
    val totalShelves = maxOf(MinShelves, rows.size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ShelfBg)
    ) {
        ShelfHeader(onSearchClick = onSearchClick, onAddClick = onAddClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(totalShelves) { index ->
                ShelfRow(
                    books = rows.getOrNull(index) ?: emptyList(),
                    rowIndex = index,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

@Composable
private fun ShelfHeader(onSearchClick: () -> Unit, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Bookshelf",
            fontFamily = PlayfairDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = InkBlack
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onSearchClick) {
            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = InkBlack)
        }
        IconButton(onClick = onAddClick) {
            Icon(Icons.Rounded.Add, contentDescription = "Add book", tint = InkBlack)
        }
    }
}

@Composable
private fun ShelfRow(
    books: List<SavedBook>,
    rowIndex: Int,
    onBookClick: (Int) -> Unit
) {
    // Alternate shelves get 1-3 books lying flat (deterministic per rowIndex)
    val lyingCount = if (rowIndex % 2 == 1 && books.size >= 3) {
        minOf(books.size - 2, (rowIndex % 3) + 1)
    } else 0
    val standingBooks = books.dropLast(lyingCount)
    val lyingBooks = books.takeLast(lyingCount)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            standingBooks.forEach { book ->
                ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
            }

            if (lyingBooks.isNotEmpty()) {
                Spacer(Modifier.width(6.dp))
                LyingBookStack(
                    books = lyingBooks,
                    onBookClick = onBookClick
                )
            }

            // Pattern repeats every 4 shelves: [none, plant, frame, none]
            if (books.size < 7) {
                when (rowIndex % 4) {
                    1 -> {
                        Spacer(Modifier.weight(1f))
                        PlantDecoration()
                    }
                    2 -> {
                        Spacer(Modifier.weight(1f))
                        PictureFrame(rowIndex = rowIndex)
                    }
                }
            }
        }

        WoodShelfBoard()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LyingBookStack(
    books: List<SavedBook>,
    onBookClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        books.forEach { book ->
            LyingBook(book = book, onClick = { onBookClick(book.id) })
        }
    }
}

@Composable
private fun LyingBook(book: SavedBook, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    val width = (48 + seed % 22).dp
    val height = (11 + seed % 4).dp
    val style = SpinePalette[seed % SpinePalette.size]

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(2.dp))
            .background(style.main)
            .clickable(onClick = onClick)
    ) {
        // Spine sliver on left (the book's spine peeking out)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(6.dp)
                .background(style.accent)
        )
        // Top edge highlight (page tops)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.2f))
        )
        // Bottom shadow under book
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black.copy(alpha = 0.22f))
        )
    }
}

@Composable
private fun WoodShelfBoard(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(16.dp)
    ) {
        val shadowH = 3.dp.toPx()
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x55000000), Color.Transparent),
                startY = 0f,
                endY = shadowH
            ),
            size = Size(size.width, shadowH)
        )
        drawRect(
            color = ShelfWood,
            topLeft = Offset(0f, shadowH),
            size = Size(size.width, size.height - shadowH)
        )
        drawLine(
            color = Color.White.copy(alpha = 0.35f),
            start = Offset(0f, shadowH + 0.5.dp.toPx()),
            end = Offset(size.width, shadowH + 0.5.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )
        val grainColor = ShelfWoodGrain.copy(alpha = 0.25f)
        listOf(0.35f, 0.55f, 0.78f).forEach { f ->
            val y = shadowH + (size.height - shadowH) * f
            drawLine(
                color = grainColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 0.6.dp.toPx()
            )
        }
        drawLine(
            color = ShelfWoodEdge,
            start = Offset(0f, size.height - 0.5.dp.toPx()),
            end = Offset(size.width, size.height - 0.5.dp.toPx()),
            strokeWidth = 1.5.dp.toPx()
        )
    }
}

@Composable
private fun ShelfBookSpine(book: SavedBook, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    val height = (115 + seed % 50).dp
    val width = (24 + (seed / 13) % 18).dp
    val style = SpinePalette[seed % SpinePalette.size]
    val hasCover = !book.coverUrl.isNullOrBlank()

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
            .background(style.main)
            .clickable(onClick = onClick)
    ) {
        if (hasCover) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(height * 0.55f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(style.accent.copy(alpha = 0.9f))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(style.accent.copy(alpha = 0.9f))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxHeight()
                .width(1.5.dp)
                .background(Color.White.copy(alpha = 0.18f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.Black.copy(alpha = 0.22f))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 14.dp, bottom = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = book.title.uppercase(),
                modifier = Modifier
                    .vertical()
                    .rotate(-90f),
                color = if (hasCover) Color.White else style.text,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontFamily = PlayfairDisplay,
                letterSpacing = 1.sp
            )
        }
    }
}

private fun Modifier.vertical() = this.layout { measurable, constraints ->
    val placeable = measurable.measure(
        Constraints(
            minWidth = constraints.minHeight,
            maxWidth = constraints.maxHeight,
            minHeight = constraints.minWidth,
            maxHeight = constraints.maxWidth
        )
    )
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}

@Composable
private fun PlantDecoration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(150.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.LocalFlorist,
            contentDescription = null,
            tint = Color(0xFF6B8E4E),
            modifier = Modifier.size(64.dp)
        )

        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 42.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                .background(Color(0xFFE8DECF))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(0xFFD7CCC8))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(Color(0x18000000))
            )
        }
    }
}

@Composable
private fun PictureFrame(rowIndex: Int) {
    val artwork = GenreArtwork[rowIndex % GenreArtwork.size]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(110.dp)
    ) {
        Box(
            modifier = Modifier.size(width = 76.dp, height = 92.dp)
        ) {
            // Layered wood frame: dark edge -> medium body -> cream matting -> artwork
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF4E342E))
                    .padding(2.dp)
                    .background(Color(0xFF8D6E63))
                    .padding(5.dp)
                    .background(Color(0xFFF5F0E8))
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(artwork),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Glass shine across top
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 9.dp)
                    .fillMaxWidth(0.65f)
                    .height(1.5.dp)
                    .background(Color.White.copy(alpha = 0.45f))
            )
            // Inner shadow under top frame edge (depth)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 8.dp, vertical = 7.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black.copy(alpha = 0.18f))
            )
        }
    }
}
