package com.aston.news.data.sources

import retrofit2.http.GET

interface SourcesAPI {

    @GET("/v2/top-headlines/sources")
    suspend fun fetch(): SourcesRemote

}