package com.example.bookvault.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookvault.domain.model.SavedBook
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
private val ShelfBoard = Color(0xFFE8DECF)
private val ShelfBoardShadow = Color(0x14000000)
private val InkBlack = Color(0xFF2C2C2C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(
    viewModel: BookViewModel,
    onBookClick: (Int) -> Unit,
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = ShelfBg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ShelfBg),
                title = {
                    Text(
                        text = "My Bookshelf",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = InkBlack
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Rounded.Search, contentDescription = "Search", tint = InkBlack)
                    }
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Rounded.Add, contentDescription = "Add book", tint = InkBlack)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            val rows = uiState.savedBooks.chunked(8)
            val totalShelves = maxOf(MinShelves, rows.size)
            items(totalShelves) { index ->
                ShelfRow(
                    books = rows.getOrNull(index) ?: emptyList(),
                    rowIndex = index,
                    onBookClick = onBookClick
                )
            }
        }
    }
}

private const val MinShelves = 4

@Composable
private fun ShelfRow(
    books: List<SavedBook>,
    rowIndex: Int,
    onBookClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            books.forEach { book ->
                ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
            }

            if (books.size < 7) {
                Spacer(Modifier.weight(1f))
                when (rowIndex % 3) {
                    0 -> StackedBooks(seed = rowIndex)
                    1 -> PlantDecoration()
                    else -> PictureFrame()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(5.dp)
                .background(ShelfBoard)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(2.dp)
                .background(ShelfBoardShadow)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ShelfBookSpine(book: SavedBook, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    val height = (115 + seed % 50).dp
    val width = (24 + (seed / 13) % 18).dp
    val style = SpinePalette[seed % SpinePalette.size]

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
            .background(style.main)
            .clickable(onClick = onClick)
    ) {
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
                color = style.text,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
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
private fun StackedBooks(seed: Int) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(70.dp)
    ) {
        repeat(3) { i ->
            val s = abs((seed * 31 + i * 7).hashCode())
            val style = SpinePalette[s % SpinePalette.size]
            val w = (44 + s % 18).dp
            Box(
                modifier = Modifier
                    .width(w)
                    .height(14.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(style.main)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(style.accent)
                )
            }
            Spacer(Modifier.height(3.dp))
        }
    }
}

@Composable
private fun PlantDecoration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(135.dp)
    ) {
        Box(
            modifier = Modifier.size(width = 72.dp, height = 90.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Frond(rotation = -55f, height = 65.dp, color = Color(0xFF4A6B3A))
            Frond(rotation = -32f, height = 75.dp, color = Color(0xFF6B8E4E))
            Frond(rotation = -10f, height = 82.dp, color = Color(0xFF4A6B3A))
            Frond(rotation = 10f, height = 82.dp, color = Color(0xFF6B8E4E))
            Frond(rotation = 32f, height = 75.dp, color = Color(0xFF4A6B3A))
            Frond(rotation = 55f, height = 65.dp, color = Color(0xFF6B8E4E))
        }
        Box(
            modifier = Modifier
                .size(width = 38.dp, height = 32.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(Color(0xFFF5F0E8))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color(0xFFE0D5C0))
            )
        }
    }
}

@Composable
private fun Frond(rotation: Float, height: Dp, color: Color) {
    Box(
        modifier = Modifier
            .width(12.dp)
            .height(height)
            .graphicsLayer {
                rotationZ = rotation
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
            .clip(RoundedCornerShape(topStartPercent = 50, topEndPercent = 50))
            .background(color)
    )
}

@Composable
private fun PictureFrame() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 72.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF8D6E63))
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F0E8)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFBCAAA4))
                )
            }
        }
    }
}
