package com.aston.news.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Filter(
    val sort: ArticleSort? = null,
    val date: Pair<Long, Long>? = null,
    val language: ArticleLanguage? = null
)