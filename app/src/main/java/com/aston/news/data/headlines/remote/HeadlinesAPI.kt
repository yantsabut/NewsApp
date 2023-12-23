package com.aston.news.data.headlines.remote

import com.aston.news.Constants.LIMIT
import com.aston.news.domain.Categories
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HeadlinesAPI {

    @GET("v2/top-headlines")
    fun fetchEverything(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("language") lang: String,
        @Query("sortBy") sort: String,
        @Query("pageSize") pageSize: Int = LIMIT,
        @Query("page") page: Int = 1,
        @Query("sources") sources: String
    ): Single<ArticleRemote>

    @GET("v2/top-headlines")
    fun fetchTopHeadlinesByCategory(
        @Query("q") query: String,
        @Query("category") type: Categories = Categories.GENERAL,
        @Query("pageSize") pageSize: Int = LIMIT,
        @Query("page") page: Int = 1,
    ): Single<ArticleRemote>

    @GET("v2/top-headlines")
    fun fetchTopHeadlinesBySource(
        @Query("q") query: String,
        @Query("sources") source: String,
        @Query("pageSize") pageSize: Int = LIMIT,
        @Query("page") page: Int = 1,
    ): Single<ArticleRemote>

}