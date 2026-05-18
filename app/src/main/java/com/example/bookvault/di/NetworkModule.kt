package com.example.bookvault.di

import com.example.bookvault.data.remote.BookApiService
import com.example.bookvault.data.remote.HttpClientFactory
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create() }
    single { BookApiService(get()) }
}