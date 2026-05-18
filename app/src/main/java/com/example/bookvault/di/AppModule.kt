package com.example.bookvault.di

import com.example.bookvault.data.repository.BookRepositoryImpl
import com.example.bookvault.data.repository.SavedBookRepositoryImpl
import com.example.bookvault.domain.repository.BookRepository
import com.example.bookvault.domain.repository.SavedBookRepository
import com.example.bookvault.domain.usecase.AddBookUseCase
import com.example.bookvault.domain.usecase.DeleteBookUseCase
import com.example.bookvault.domain.usecase.DeleteSavedBookUseCase
import com.example.bookvault.domain.usecase.GetBookByIdUseCase
import com.example.bookvault.domain.usecase.GetBooksUseCase
import com.example.bookvault.domain.usecase.GetSavedBooksUseCase
import com.example.bookvault.domain.usecase.IsBookSavedUseCase
import com.example.bookvault.domain.usecase.SaveBookUseCase
import com.example.bookvault.presentation.viewmodel.BookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Repositories
    single<BookRepository> { BookRepositoryImpl(get(), get()) }
    single<SavedBookRepository> { SavedBookRepositoryImpl(get()) }

    // API UseCases
    factory { GetBooksUseCase(get()) }
    factory { GetBookByIdUseCase(get()) }
    factory { AddBookUseCase(get()) }
    factory { DeleteBookUseCase(get()) }

    // Saved Books UseCases
    factory { GetSavedBooksUseCase(get()) }
    factory { SaveBookUseCase(get()) }
    factory { DeleteSavedBookUseCase(get()) }
    factory { IsBookSavedUseCase(get()) }

    // ViewModel
    viewModel {
        BookViewModel(
            getBooks = get(),
            getBookById = get(),
            addBook = get(),
            deleteBook = get(),
            getSavedBooks = get(),
            saveBook = get(),
            deleteSavedBook = get(),
            isBookSaved = get()
        )
    }
}