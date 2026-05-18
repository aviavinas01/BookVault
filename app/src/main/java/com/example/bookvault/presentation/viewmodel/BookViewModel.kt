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
import kotlinx.coroutines.launch

data class BookUiState(
    // API books (for browse screen)
    val books: List<Book> = emptyList(),
    val selectedBook: Book? = null,
    // Saved books (personal list)
    val savedBooks: List<SavedBook> = emptyList(),
    val savedBookIds: Set<Int> = emptySet(),
    // UI state
    val isLoading: Boolean = false,
    val isBrowseLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val saveSuccess: Boolean = false
)

class BookViewModel(
    private val getBooks: GetBooksUseCase,
    private val getBookById: GetBookByIdUseCase,
    private val addBook: AddBookUseCase,
    private val deleteBook: DeleteBookUseCase,
    private val getSavedBooks: GetSavedBooksUseCase,
    private val saveBook: SaveBookUseCase,
    private val deleteSavedBook: DeleteSavedBookUseCase,
    private val isBookSaved: IsBookSavedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        fetchBooks()
        observeSavedBooks()
    }

    // Observe saved books from Room as a Flow
    // UI updates automatically when anything changes
    private fun observeSavedBooks() {
        viewModelScope.launch {
            getSavedBooks().collect { saved ->
                _uiState.value = _uiState.value.copy(
                    savedBooks = saved,
                    savedBookIds = saved.map { it.id }.toSet()
                )
            }
        }
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isBrowseLoading = true,
                error = null
            )
            getBooks().fold(
                onSuccess = { books ->
                    _uiState.value = _uiState.value.copy(
                        books = books,
                        isBrowseLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isBrowseLoading = false,
                        error = error.message ?: "Could not load books"
                    )
                }
            )
        }
    }

    fun fetchBookById(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getBookById(id).fold(
                onSuccess = { book ->
                    _uiState.value = _uiState.value.copy(
                        selectedBook = book,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    fun saveBookToList(book: Book) {
        viewModelScope.launch {
            saveBook(book).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(saveSuccess = true)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
            )
        }
    }

    fun removeFromList(id: Int) {
        viewModelScope.launch {
            deleteSavedBook(id)
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            addBook.invoke(book).fold(
                onSuccess = { saved ->
                    saveBook(saved)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    fun deleteBook(id: Int) {
        viewModelScope.launch {
            deleteBook.invoke(id)
            deleteSavedBook(id)
        }
    }

    fun isBookSaved(id: Int): Boolean =
        _uiState.value.savedBookIds.contains(id)

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}