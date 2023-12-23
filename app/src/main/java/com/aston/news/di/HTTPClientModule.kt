package com.aston.news.di

import com.aston.news.BuildConfig
import com.aston.news.core.network.NetworkUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
object HTTPClientModule {

    @Provides
    fun provideHttpBuilder() = OkHttpClient.Builder()

    @Provides
    fun provideDefaultClient(builder: OkHttpClient.Builder):OkHttpClient = builder
        .apply {
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                addInterceptor(logger)
            }
        }
        .addInterceptor { NetworkUtils.apiKeyAsHeader(it) }
        .build()
}