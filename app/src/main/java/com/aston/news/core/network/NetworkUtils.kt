package com.aston.news.core.network

import okhttp3.Interceptor

object NetworkUtils {

    fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .url(chain.request().url.newBuilder().addQueryParameter("apiKey", ApiEndpoints.API_KEY).build())
            .build()
    )

    fun apiKeyAsHeader(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", ApiEndpoints.API_KEY)
            .build()
    )

    fun getSourceIconUrl(domain: String): String {
        val modifiedUrl = domain
            .replace("http://", "")
            .replace("https://","")
            .split("/")
            .first()

        return ApiEndpoints.SOURCE_ICON_URL + modifiedUrl
    }

}