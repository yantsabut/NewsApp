package com.aston.news.utills

import io.reactivex.Observable

interface PagingListener {
    fun <T> onNextPage(offset: Int): Observable<T>
}