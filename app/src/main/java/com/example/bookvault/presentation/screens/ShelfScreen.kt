package com.example.bookvault.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.presentation.viewmodel.BookViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(
    viewModel: BookViewModel,
    onBookClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Bookshelf",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            // Bookcase Frame Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 8.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            )

            if (uiState.savedBooks.isEmpty()) {
                EmptyShelfState(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
                ) {
                    val chunkedBooks = uiState.savedBooks.chunked(6)
                    items(chunkedBooks.size) { index ->
                        ShelfRow(
                            books = chunkedBooks[index],
                            rowIndex = index,
                            onBookClick = onBookClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyShelfState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your bookshelf is empty",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Save books and they will appear here naturally.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
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
                .height(160.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            books.forEach { book ->
                ShelfBookSpine(
                    book = book,
                    onClick = { onBookClick(book.id) }
                )
                Spacer(Modifier.width(4.dp))
            }

            // If shelf is not full, randomly add a decoration based on rowIndex
            if (books.size < 5) {
                Spacer(Modifier.weight(1f))
                if (rowIndex % 2 == 0) {
                    PlantDecoration()
                } else {
                    FrameDecoration()
                }
                Spacer(Modifier.width(16.dp))
            }
        }

        // Shelf Board
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        // Shadow under shelf
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color.Black.copy(alpha = 0.1f))
        )
    }
}

@Composable
private fun ShelfBookSpine(
    book: SavedBook,
    onClick: () -> Unit
) {
    // Determine dimensions pseudo-randomly based on ID to maintain consistency
    val baseHeight = 100
    val heightVar = (abs(book.id.hashCode()) % 50)
    val height = (baseHeight + heightVar).dp
    
    val baseWidth = 28
    val widthVar = (abs(book.title.hashCode()) % 16)
    val width = (baseWidth + widthVar).dp
    
    val spineColor = when (abs(book.id.hashCode()) % 6) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        2 -> MaterialTheme.colorScheme.tertiary
        3 -> Color(0xFF2E4A35) // Deep Green
        4 -> Color(0xFF800020) // Burgundy
        else -> MaterialTheme.colorScheme.inverseSurface
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .background(spineColor)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Spine design lines
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.3f))
            )
            Text(
                text = book.title,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
private fun PlantDecoration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(70.dp)
    ) {
        // Leaves
        Box(
            modifier = Modifier
                .size(width = 30.dp, height = 40.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(Color(0xFF4CAF50))
        )
        // Pot
        Box(
            modifier = Modifier
                .size(width = 24.dp, height = 30.dp)
                .clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                .background(Color(0xFFD7CCC8))
        )
    }
}

@Composable
private fun FrameDecoration() {
    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 55.dp)
            .border(4.dp, Color(0xFF8D6E63), RoundedCornerShape(2.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFFBCAAA4), RoundedCornerShape(10.dp))
        )
    }
}
