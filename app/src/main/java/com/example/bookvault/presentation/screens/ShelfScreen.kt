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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/* ----------------------------------------------------------------------------
 * Spine colour styles. A spine carries a main body colour, an accent used for
 * decorative head/tail bands, and the colour the title prints in.
 * ------------------------------------------------------------------------- */
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

/* Bright, airy palette — matches the white floating-shelf reference. */
private val ShelfBg = Color(0xFFFDFBF7)          // page background
private val WallBg = Color(0xFFFBF8F2)            // back wall inside the alcove
private val WallShadeTop = Color(0x14000000)      // soft shadow the upper shelf casts
private val WallShadeSide = Color(0x0F000000)     // faint side ambient occlusion
private val ContactShadow = Color(0x33000000)     // where books meet the board

private val BoardTop = Color(0xFFF3ECDF)          // lit top face of the shelf
private val BoardFace = Color(0xFFE7DCC8)         // front edge of the board
private val BoardUnder = Color(0xFFC9BBA3)        // underside line
private val BoardHighlight = Color(0xCCFFFFFF)    // top catch-light

private val PageCream = Color(0xFFF3EAD8)         // visible page block on lying books
private val InkBlack = Color(0xFF2C2C2C)

private const val MinShelves = 4
// 4 books per shelf — each shelf is a single arrangement type.
// Cycle of 3: vertical shelf (4 standing) → horizontal shelf (4 lying) → vertical shelf (4 standing).
private const val BooksPerShelf = 4

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
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ShelfHeader(onSearchClick: () -> Unit, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 4.dp),
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
    Column(modifier = Modifier.fillMaxWidth()) {
        ShelfAlcove {
            // 3-shelf cycle, one arrangement per shelf, books in saved order.
            // 0 -> 4 standing books on right, marble bookend on left
            // 1 -> 4 lying books on left, potted plant on right
            // 2 -> 4 standing books on right, picture frame on left
            when (rowIndex % 3) {
                0 -> {
                    Spacer(Modifier.weight(1f))
                    MarbleBookend()
                    Spacer(Modifier.width(2.dp))
                    books.forEach { book ->
                        ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
                    }
                }
                1 -> {
                    if (books.isNotEmpty()) {
                        HorizontalBookStack(books = books, onBookClick = onBookClick)
                    }
                    Spacer(Modifier.weight(1f))
                    PottedPlant()
                }
                else -> {
                    EmptyPictureFrame()
                    Spacer(Modifier.weight(1f))
                    books.forEach { book ->
                        ShelfBookSpine(book = book, onClick = { onBookClick(book.id) })
                    }
                }
            }
        }
        ShelfBoard()
        Spacer(modifier = Modifier.height(6.dp))
    }
}

/* ----------------------------------------------------------------------------
 * The recessed wall the books sit against. Bright and clean like the reference,
 * with soft directional shadows that give the niche real depth.
 * ------------------------------------------------------------------------- */
@Composable
private fun ShelfAlcove(content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(196.dp)
            .background(WallBg)
    ) {
        // Shadow the shelf above casts onto the back wall
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(26.dp)
                .background(Brush.verticalGradient(listOf(WallShadeTop, Color.Transparent)))
        )
        // Gentle side ambient occlusion
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(16.dp)
                .background(Brush.horizontalGradient(listOf(WallShadeSide, Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(16.dp)
                .background(Brush.horizontalGradient(listOf(Color.Transparent, WallShadeSide)))
        )
        // Contact shadow strip — grounds whatever stands on the board
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, ContactShadow)))
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.Bottom,
            content = content
        )
    }
}

/* ----------------------------------------------------------------------------
 * A clean white floating shelf: a thin lit board, a darker front edge, and a
 * soft cast shadow underneath — the shadow is what makes it read as floating.
 * ------------------------------------------------------------------------- */
@Composable
private fun ShelfBoard(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
    ) {
        val boardH = 13.dp.toPx()

        // Top lit face of the board
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(BoardTop, BoardFace),
                startY = 0f,
                endY = boardH
            ),
            size = Size(size.width, boardH)
        )
        // Catch-light along the very top edge
        drawRect(
            color = BoardHighlight,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, 1.2.dp.toPx())
        )
        // Underside line — the board has thickness
        drawRect(
            color = BoardUnder,
            topLeft = Offset(0f, boardH - 1.4.dp.toPx()),
            size = Size(size.width, 1.4.dp.toPx())
        )
        // Soft shadow the shelf casts on the wall below it (floating effect)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x26000000), Color.Transparent),
                startY = boardH,
                endY = size.height
            ),
            topLeft = Offset(0f, boardH),
            size = Size(size.width, size.height - boardH)
        )
    }
}

/* ----------------------------------------------------------------------------
 * A standing book. The realism comes from the horizontal "rounded spine" sheen
 * (a real spine is a cylinder catching light down its centre), the lit/shadowed
 * vertical edges that separate neighbours, and varied head/tail banding.
 * ------------------------------------------------------------------------- */
@Composable
private fun ShelfBookSpine(book: SavedBook, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    val height = (122 + seed % 32).dp
    val width = (28 + (seed / 13) % 14).dp  // 28-41 dp — back to full-size spine widths
    val style = SpinePalette[seed % SpinePalette.size]
    val template = seed % 3
    val hasCover = !book.coverUrl.isNullOrBlank()

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp, bottomStart = 1.dp, bottomEnd = 1.dp))
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
                    .height(height * 0.5f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f))
                        )
                    )
            )
        } else {
            // Decorative head/tail treatment, varied per book
            when (template) {
                0 -> {
                    SpineBand(Alignment.TopCenter, style.accent, 9.dp)
                    SpineBand(Alignment.BottomCenter, style.accent, 13.dp)
                }
                1 -> {
                    // A single accent block across the upper third
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(height * 0.3f)
                            .background(style.accent.copy(alpha = 0.92f))
                    )
                }
                else -> {
                    SpineBand(Alignment.TopCenter, style.accent, 7.dp)
                    // thin author/publisher rule near the foot
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 9.dp)
                            .fillMaxWidth(0.55f)
                            .height(1.5.dp)
                            .background(style.accent.copy(alpha = 0.85f))
                    )
                }
            }
        }

        // Rounded-spine sheen: dark edges, a soft highlight running down the centre.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        0.00f to Color.Black.copy(alpha = 0.22f),
                        0.14f to Color.Transparent,
                        0.50f to Color.White.copy(alpha = 0.10f),
                        0.86f to Color.Transparent,
                        1.00f to Color.Black.copy(alpha = 0.30f)
                    )
                )
        )
        // Crisp lit edge (left) and shadow gap (right) to separate from neighbours
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(1.2.dp)
                .background(Color.White.copy(alpha = 0.20f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.Black.copy(alpha = 0.26f))
        )

        // Title runs vertically up the spine, as on a real book
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 18.dp),
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

@Composable
private fun androidx.compose.foundation.layout.BoxScope.SpineBand(
    align: Alignment,
    color: Color,
    bandHeight: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .align(align)
            .fillMaxWidth()
            .height(bandHeight)
            .background(color.copy(alpha = 0.92f))
    )
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

/* ----------------------------------------------------------------------------
 * A horizontal pile of lying books. Each book is the standing spine transposed,
 * given a visible cream page block and a tiny hand-stacked jitter, with a soft
 * shadow under the pile so it sits on the board.
 * ------------------------------------------------------------------------- */
@Composable

private fun HorizontalBookStack(
    books: List<SavedBook>,
    onBookClick: (Int) -> Unit
) {
    // IntrinsicSize.Max bounds the column to the widest book — without this, the
    // fillMaxWidth contact-shadow below would propagate up and eat the whole Row,
    // pushing the plant out to 0 dp width.
    Column(
        modifier = Modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        books.forEachIndexed { index, book ->
            HorizontalBookSpine(
                book = book,
                indexInStack = index,
                onClick = { onBookClick(book.id) }
            )
        }
        // Contact shadow pooled under the whole pile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Brush.verticalGradient(listOf(Color(0x30000000), Color.Transparent)))
        )
    }
}

@Composable
private fun HorizontalBookSpine(book: SavedBook, indexInStack: Int, onClick: () -> Unit) {
    val seed = abs(book.id.hashCode() xor book.title.hashCode())
    // Same data as the standing spine, transposed 90°
    val length = (108 + seed % 26).dp
    val thickness = (24 + (seed / 13) % 11).dp
    val style = SpinePalette[seed % SpinePalette.size]
    val jitter = ((seed % 7) - 3).dp   // -3..+3 dp, looks hand-stacked

    Box(
        modifier = Modifier
            .offset(x = jitter)
            .width(length)
            .height(thickness)
            .clip(RoundedCornerShape(2.dp))
            .background(style.main)
            .clickable(onClick = onClick)
    ) {
        // Cream page block on the right end (the open pages of a lying book)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(7.dp)
                .background(PageCream)
        )
        // Head/tail accent bands at the spine ends
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(11.dp)
                .background(style.accent.copy(alpha = 0.9f))
        )
        // Light along the top edge, shadow under the bottom
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.4.dp)
                .background(Color.White.copy(alpha = 0.22f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black.copy(alpha = 0.24f))
        )
        // Subtle cylindrical shading top-to-bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Black.copy(alpha = 0.10f),
                        0.5f to Color.White.copy(alpha = 0.06f),
                        1.0f to Color.Black.copy(alpha = 0.14f)
                    )
                )
        )
        // Title reads horizontally
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 18.dp, end = 12.dp),
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

/* ----------------------------------------------------------------------------
 * Imported vase + flower image. Transparent PNG drops in cleanly over the
 * shelf because ContentScale.Fit preserves aspect inside the 96×170 box.
 * ------------------------------------------------------------------------- */
@Composable
private fun PottedPlant() {
    Image(
        painter = painterResource(R.drawable.plant_vase),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        // BottomCenter anchors the vase to the shelf surface so it never appears
        // to float when the PNG's aspect is shorter than the bounding box.
        alignment = Alignment.BottomCenter,
        modifier = Modifier
            .height(195.dp)
            .width(130.dp)
    )
}

/* ----------------------------------------------------------------------------
 * A white marble bookend — a right-triangle block that caps a run of standing
 * books, like the one on the reference's middle shelf.
 * ------------------------------------------------------------------------- */
@Composable
private fun MarbleBookend() {
    Canvas(
        modifier = Modifier
            .height(112.dp)
            .width(40.dp)
    ) {
        val w = size.width
        val h = size.height

        // Cast shadow to the right
        val shadow = Path().apply {
            moveTo(4.dp.toPx(), h * 0.22f)
            lineTo(w, h)
            lineTo(4.dp.toPx(), h)
            close()
        }
        drawPath(shadow, color = Color(0x1A000000))

        // Marble triangle: tall vertical edge on the left, sloping to the right
        val block = Path().apply {
            moveTo(0f, h * 0.18f)
            lineTo(0f, h)
            lineTo(w, h)
            close()
        }
        drawPath(block, color = Color(0xFFF6F3EE))
        // Subtle top-lit face
        drawPath(
            block,
            brush = Brush.verticalGradient(
                listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)
            )
        )
        // Faint grey veining
        drawLine(
            color = Color(0x22746A60),
            start = Offset(0f, h * 0.55f),
            end = Offset(w * 0.7f, h * 0.95f),
            strokeWidth = 1.2f
        )
        drawLine(
            color = Color(0x18746A60),
            start = Offset(0f, h * 0.72f),
            end = Offset(w * 0.45f, h),
            strokeWidth = 1f
        )
    }
}

/* ----------------------------------------------------------------------------
 * A framed piece of wall art: layered wood frame, cream matting, soft glass
 * shine. Kept from the original layout, rebuilt to read as a real frame.
 * ------------------------------------------------------------------------- */
@Composable
private fun EmptyPictureFrame() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(112.dp)
    ) {
        Box(modifier = Modifier.size(width = 72.dp, height = 92.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF4E342E))   // dark outer edge
                    .padding(2.dp)
                    .background(Color(0xFF8D6E63))   // wood body
                    .padding(6.dp)
                    .background(Color(0xFFF7F2EA))   // cream matting
            )
            // Glass shine streak
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 14.dp, top = 12.dp)
                    .fillMaxWidth(0.45f)
                    .height(1.5.dp)
                    .background(Color.White.copy(alpha = 0.55f))
            )
            // Inner shadow for depth under the frame lip
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 9.dp, vertical = 9.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black.copy(alpha = 0.16f))
            )
        }
    }
}