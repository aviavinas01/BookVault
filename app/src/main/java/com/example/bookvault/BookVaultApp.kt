package com.example.bookvault

import android.app.Application
import com.example.bookvault.di.appModule
import com.example.bookvault.di.databaseModule
import com.example.bookvault.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BookVaultApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BookVaultApp)
            modules(
                networkModule,
                databaseModule,
                appModule
            )
        }
    }
}