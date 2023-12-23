package com.aston.news.data.headlines.remote

import com.aston.news.domain.Article

data class ArticleRemote (
    val status: String,
    val totalResult: Int,
    val articles: List<Article>
)