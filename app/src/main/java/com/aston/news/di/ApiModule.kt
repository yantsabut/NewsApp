package com.aston.news.di

import com.aston.news.core.network.APIBuilder
import com.aston.news.data.headlines.remote.HeadlinesAPI
import com.aston.news.data.sources.SourcesAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ApiModule {

    @Provides
    fun provideHeadlineApi(
        defaultHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) = APIBuilder.createApiService(HeadlinesAPI::class.java, defaultHttpClient, gsonConverterFactory)

    @Provides
    fun provideSourcesApi(
        defaultHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) = APIBuilder.createApiService(SourcesAPI::class.java, defaultHttpClient, gsonConverterFactory)


}