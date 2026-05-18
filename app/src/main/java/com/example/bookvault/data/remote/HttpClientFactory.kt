package com.example.bookvault.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    coerceInputValues = true    // handles null/missing fields
                })
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }
    }
}