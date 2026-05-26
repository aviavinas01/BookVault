package com.example.bookvault.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.presentation.viewmodel.BookViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook

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
        if (uiState.savedBooks.isEmpty()) {
            EmptyShelfState(modifier = Modifier
                .fillMaxSize()
                .padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(uiState.savedBooks.chunked(5)) { shelfRow ->
                    ShelfRow(
                        books = shelfRow,
                        onBookClick = onBookClick
                    )
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surface
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Your bookshelf is empty",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Save books and they will appear here as a real shelf.",
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
    onBookClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            books.forEach { book ->
                ShelfBookSpine(
                    book = book,
                    onClick = { onBookClick(book.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
private fun ShelfBookSpine(
    book: SavedBook,
    onClick: () -> Unit
) {
    val height = 140.dp + ((book.pageCount % 5) * 12).dp
    val width = when (book.title.length % 4) {
        0 -> 34.dp
        1 -> 40.dp
        2 -> 36.dp
        else -> 42.dp
    }
    val spineColor = when (book.id % 5) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        2 -> MaterialTheme.colorScheme.tertiary
        3 -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.inverseSurface
    }

    Card(
        modifier = Modifier
            .width(width)
            .height(height)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = spineColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = book.title.take(18),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${book.pageCount}p",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
    }
}
