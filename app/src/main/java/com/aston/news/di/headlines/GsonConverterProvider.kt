package com.aston.news.di.headlines

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory

@Module
object GsonConverterProvider {

    @Provides
    fun providesGsonConverter(): GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    )

}