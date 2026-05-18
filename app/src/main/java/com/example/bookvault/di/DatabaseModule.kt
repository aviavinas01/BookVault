package com.example.bookvault.di

import androidx.room.Room
import com.example.bookvault.data.local.BookDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            BookDatabase::class.java,
            "book_vault_db"
        ).build()
    }
    single { get<BookDatabase>().bookDao() }
}