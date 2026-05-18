package com.example.bookvault.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookvault.domain.model.Book
import com.example.bookvault.domain.usecase.AddBookUseCase
import com.example.bookvault.domain.usecase.DeleteBookUseCase
import com.example.bookvault.domain.usecase.GetBookByIdUseCase
import com.example.bookvault.domain.usecase.GetBooksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookUiState(
    val books: List<Book> = emptyList(),
    val selectedBook: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class BookViewModel(
    private val getBooks: GetBooksUseCase,
    private val getBookById: GetBookByIdUseCase,
    private val addBook: AddBookUseCase,
    private val deleteBook: DeleteBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getBooks().fold(
                onSuccess = { books ->
                    _uiState.value = _uiState.value.copy(
                        books = books,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Something went wrong"
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

    fun addBook(book: Book) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            addBook.invoke(book).fold(
                onSuccess = {
                    fetchBooks()
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
            _uiState.value = _uiState.value.copy(isLoading = true)
            deleteBook.invoke(id).fold(
                onSuccess = {
                    fetchBooks()
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}