package com.example.bookvault.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.model.SavedBook
import com.example.bookvault.domain.usecase.AddBookUseCase
import com.example.bookvault.domain.usecase.DeleteBookUseCase
import com.example.bookvault.domain.usecase.DeleteSavedBookUseCase
import com.example.bookvault.domain.usecase.GetBookByIdUseCase
import com.example.bookvault.domain.usecase.GetBooksUseCase
import com.example.bookvault.domain.usecase.GetSavedBooksUseCase
import com.example.bookvault.domain.usecase.IsBookSavedUseCase
import com.example.bookvault.domain.usecase.SaveBookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookUiState(
    val books: List<Book> = emptyList(),
    val savedBooks: List<SavedBook> = emptyList(),
    val savedBookIds: Set<Int> = emptySet(),
    val selectedBook: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class BookViewModel(
    private val getBooks: GetBooksUseCase,
    private val getBookById: GetBookByIdUseCase,
    private val addBook: AddBookUseCase,
    private val deleteBook: DeleteBookUseCase,
    private val getSavedBooks: GetSavedBooksUseCase,
    private val saveBook: SaveBookUseCase,
    private val deleteSavedBook: DeleteSavedBookUseCase,
    @Suppress("UnusedPrivateMember")
    private val isBookSaved: IsBookSavedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        fetchBooks()
        observeSavedBooks()
    }

    private fun observeSavedBooks() {
        getSavedBooks().onEach { saved ->
            _uiState.update { it.copy(
                savedBooks = saved,
                savedBookIds = saved.map { it.id }.toSet()
            ) }
        }.launchIn(viewModelScope)
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getBooks().fold(
                onSuccess = { books ->
                    _uiState.update { it.copy(books = books, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }
                }
            )
        }
    }

    fun fetchBookById(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getBookById(id).fold(
                onSuccess = { book ->
                    _uiState.update { it.copy(selectedBook = book, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun submitBook(book: Book) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            addBook(book).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    fetchBooks()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun removeBook(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteBook(id).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    fetchBooks()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun saveBookToList(book: Book) {
        viewModelScope.launch { saveBook(book) }
    }

    fun removeFromList(id: Int) {
        viewModelScope.launch { deleteSavedBook(id) }
    }

    // Derived from the live savedBooks state — no extra DB call needed
    fun isBookSaved(id: Int): Boolean = _uiState.value.savedBooks.any { it.id == id }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}