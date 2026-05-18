package com.example.bookvault.di

import com.example.bookvault.data.repository.BookRepositoryImpl
import com.example.bookvault.domain.repository.BookRepository
import com.example.bookvault.domain.usecase.AddBookUseCase
import com.example.bookvault.domain.usecase.DeleteBookUseCase
import com.example.bookvault.domain.usecase.GetBookByIdUseCase
import com.example.bookvault.domain.usecase.GetBooksUseCase
import com.example.bookvault.presentation.viewmodel.BookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Repository — binds interface to implementation
    single<BookRepository> { BookRepositoryImpl(get(), get()) }

    // UseCases
    factory { GetBooksUseCase(get()) }
    factory { GetBookByIdUseCase(get()) }
    factory { AddBookUseCase(get()) }
    factory { DeleteBookUseCase(get()) }

    // ViewModel
    viewModel {
        BookViewModel(
            getBooks = get(),
            getBookById = get(),
            addBook = get(),
            deleteBook = get()
        )
    }
}