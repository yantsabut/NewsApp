package com.aston.news.base

sealed class DataState<out T> {
    /**
     * A generic data class to wrap the succeeded data result.
     *
     * @param data the result data
     */
    data class Success<out T>(val data: T) : DataState<T>()

    /**
     * A data class to wrap the fail exception result.
     *
     * @param exception the exception result
     */
    data class Error(val exception: Exception) : DataState<Nothing>()

    /**
     * A Nothing object that emits the loading state.
     */
    object Loading : DataState<Nothing>()

    /**
     * A Nothing object that emits the empty result state,
     * we use this if the request or the query succeeded but with empty results.
     */
    object Empty : DataState<Nothing>()

}