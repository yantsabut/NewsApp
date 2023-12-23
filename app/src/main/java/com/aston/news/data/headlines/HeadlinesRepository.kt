package com.aston.news.data.headlines

import com.aston.news.domain.Article
import com.aston.news.domain.ArticleLanguage
import com.aston.news.domain.ArticleSort
import com.aston.news.domain.Categories
import io.reactivex.Single

interface HeadlinesRepository {

    fun fetchArticles(
        type: Categories,
        page: Int = 1,
        query: String = "",
        sortBy: ArticleSort?,
        from: Long?,
        to: Long?,
        lang: ArticleLanguage?,
        source: String?
    ): Single<List<Article>>

}