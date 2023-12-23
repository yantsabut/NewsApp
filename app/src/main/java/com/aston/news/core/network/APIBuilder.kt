package com.aston.news.core.network

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object APIBuilder {

    fun <Api> createApiService(
        apiClass: Class<Api>,
        defaultHttpClient: OkHttpClient,
        gsonConverter: Converter.Factory? = null
    ): Api {
        val retrofitBuilder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(ApiEndpoints.BASE_URL)
            .client(defaultHttpClient)

        if (gsonConverter != null) retrofitBuilder.addConverterFactory(gsonConverter)

        return retrofitBuilder.build().create(apiClass)
    }

}