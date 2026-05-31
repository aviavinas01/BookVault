package com.example.bookvault.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
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
private const val BooksPerShelf = 8

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
                        fontFamily = PlayfairDisplay,
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
            val rows = uiState.savedBooks.chunked(BooksPerShelf)
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
                    else -> PictureFrame(rowIndex = rowIndex)
                }
            }
        }

        WoodShelfBoard()
        Spacer(modifier = Modifier.height(8.dp))
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
        // Soft contact shadow where books meet the shelf
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x55000000), Color.Transparent),
                startY = 0f,
                endY = shadowH
            ),
            size = Size(size.width, shadowH)
        )
        // Wood plank
        drawRect(
            color = ShelfWood,
            topLeft = Offset(0f, shadowH),
            size = Size(size.width, size.height - shadowH)
        )
        // Top edge highlight (light from above)
        drawLine(
            color = Color.White.copy(alpha = 0.35f),
            start = Offset(0f, shadowH + 0.5.dp.toPx()),
            end = Offset(size.width, shadowH + 0.5.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )
        // Grain lines
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
        // Bottom dark edge (depth under shelf)
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
            // Dark gradient at bottom so title stays readable over any cover
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

        // Light source from upper-left
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
                // Top highlight strip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.18f))
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
        modifier = Modifier.height(150.dp)
    ) {
        Box(
            modifier = Modifier.size(width = 110.dp, height = 110.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FernFrond(rotation = -60f, length = 78.dp, leafColor = Color(0xFF4A6B3A))
            FernFrond(rotation = -35f, length = 88.dp, leafColor = Color(0xFF6B8E4E))
            FernFrond(rotation = -12f, length = 95.dp, leafColor = Color(0xFF4A6B3A))
            FernFrond(rotation = 12f, length = 95.dp, leafColor = Color(0xFF6B8E4E))
            FernFrond(rotation = 35f, length = 88.dp, leafColor = Color(0xFF4A6B3A))
            FernFrond(rotation = 60f, length = 78.dp, leafColor = Color(0xFF6B8E4E))
        }
        Box(
            modifier = Modifier
                .size(width = 42.dp, height = 34.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(Color(0xFFF5F0E8))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color(0xFFE0D5C0))
            )
            // Pot shadow on right side for roundness
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(Color(0x14000000))
            )
        }
    }
}

@Composable
private fun FernFrond(rotation: Float, length: Dp, leafColor: Color) {
    val stemColor = Color(0xFF3D5A2C)
    Canvas(
        modifier = Modifier
            .width(32.dp)
            .height(length)
            .graphicsLayer {
                rotationZ = rotation
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
    ) {
        val cx = size.width / 2
        // Central stem
        drawLine(
            color = stemColor,
            start = Offset(cx, size.height),
            end = Offset(cx, size.height * 0.06f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        // Compound leaflets along the stem
        val leafletCount = 7
        val maxLeaflet = size.width * 0.45f
        for (i in 1..leafletCount) {
            val t = i.toFloat() / (leafletCount + 1)
            val y = size.height * (1f - t)
            val len = maxLeaflet * (1f - t * 0.6f)
            drawLine(
                color = leafColor,
                start = Offset(cx, y),
                end = Offset(cx - len, y - len * 0.5f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = leafColor,
                start = Offset(cx, y),
                end = Offset(cx + len, y - len * 0.5f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
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
        modifier = Modifier.height(95.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 78.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF6B4F35))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(1.dp))
                    .background(Color(0xFFF5F0E8))
            ) {
                Image(
                    painter = painterResource(artwork),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Inner shadow at top suggests glass depth
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black.copy(alpha = 0.25f))
            )
        }
    }
}
