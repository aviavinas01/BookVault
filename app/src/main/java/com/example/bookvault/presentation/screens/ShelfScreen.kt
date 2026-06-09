package com.example.bookvault.presentation.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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

private val ShelfBg = Color(0xFFFAF6F0)
private val CaveBg = Color(0xFFE6D9C2)
private val CaveDarkInner = Color(0xFFB89A75)
private val ShelfWood = Color(0xFFC9A87A)
private val ShelfWoodGrain = Color(0xFF8D6940)
private val ShelfWoodEdge = Color(0xFF6B4F35)
private val InkBlack = Color(0xFF2C2C2C)
private const val MinShelves = 4
private const val BooksPerShelf = 9

@Composable
fun ShelfScreen(
    viewModel: BookViewModel,
    onBookClick: (Int) -> Unit,
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val rows = remember(uiState.savedBooks) { uiState.savedBooks.chunked(BooksPerShelf) }
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
    val standingBooks = books.take(5)
    val horizontalBooks = books.drop(5).take(4)

    Column(modifier = Modifier.fillMaxWidth()) {
        CaveAlcove {
            // Pattern repeats every 3 shelves
            // 0 → empty-left, books-right
            // 1 → books-left, vase-right
            // 2 → frame-left, books-right
            when (rowIndex % 3) {
                0 -> {
                    Spacer(Modifier.weight(1f))
                    if (horizontalBooks.isNotEmpty()) {
                        HorizontalBookStack(books = horizontalBooks, onBookClick = onBookClick)
                        Spacer(Modifier.width(4.dp))
                    }
                    standingBooks.forEach { book ->
                        ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
                    }
                }
                1 -> {
                    standingBooks.forEach { book ->
                        ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
                    }
                    if (horizontalBooks.isNotEmpty()) {
                        Spacer(Modifier.width(4.dp))
                        HorizontalBookStack(books = horizontalBooks, onBookClick = onBookClick)
                    }
                    Spacer(Modifier.weight(1f))
                    PlantDecoration()
                }
                else -> {
                    EmptyPictureFrame()
                    Spacer(Modifier.weight(1f))
                    if (horizontalBooks.isNotEmpty()) {
                        HorizontalBookStack(books = horizontalBooks, onBookClick = onBookClick)
                        Spacer(Modifier.width(4.dp))
                    }
                    standingBooks.forEach { book ->
                        ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
                    }
                }
            }
        }
        WoodShelfBoard()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CaveAlcove(content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(CaveBg)
    ) {
        // Top overhang shadow — cave lip casts darkness on back wall
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(22.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0x38000000), Color.Transparent)
                    )
                )
        )
        // Left side wall — receding shadow
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(12.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(CaveDarkInner.copy(alpha = 0.55f), Color.Transparent)
                    )
                )
        )
        // Right side wall
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(12.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, CaveDarkInner.copy(alpha = 0.55f))
                    )
                )
        )
        // Books / decorations inside the alcove
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom,
            content = content
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
    val height = (118 + seed % 38).dp
    val width = (22 + (seed / 13) % 12).dp
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
private fun HorizontalBookStack(
    books: List<SavedBook>,
    onBookClick: (Int) -> Unit
) {
    // Each horizontal book = same spine dimensions, rotated 90°.
    // Stack of 4 forms a vertical pile bottom-aligned with standing books.
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        books.forEach { book ->
            HorizontalBookSpine(book = book, onClick = { onBookClick(book.id) })
        }
    }
}

@Composable
private fun HorizontalBookSpine(book: SavedBook, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    // Dimensions are the standing spine, transposed — same data, rotated 90°
    val width = (118 + seed % 38).dp
    val height = (22 + (seed / 13) % 12).dp
    val style = SpinePalette[seed % SpinePalette.size]

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(2.dp))
            .background(style.main)
            .clickable(onClick = onClick)
    ) {
        // Spine bands now on left & right (since the book is "lying" rotated)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(14.dp)
                .background(style.accent.copy(alpha = 0.9f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(10.dp)
                .background(style.accent.copy(alpha = 0.9f))
        )
        // Light hits the top edge of the lying book
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.5.dp)
                .background(Color.White.copy(alpha = 0.2f))
        )
        // Bottom shadow under the book
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black.copy(alpha = 0.22f))
        )
        // Title reads horizontally
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = book.title.uppercase(),
                color = style.text,
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
private fun EmptyPictureFrame() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(110.dp)
    ) {
        Box(
            modifier = Modifier.size(width = 70.dp, height = 88.dp)
        ) {
            // Layered wood frame: dark edge → wood body → cream matting (empty)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF4E342E))
                    .padding(2.dp)
                    .background(Color(0xFF8D6E63))
                    .padding(5.dp)
                    .background(Color(0xFFF5F0E8))
            )
            // Glass shine
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 9.dp)
                    .fillMaxWidth(0.6f)
                    .height(1.5.dp)
                    .background(Color.White.copy(alpha = 0.45f))
            )
            // Inner top shadow for frame depth
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
