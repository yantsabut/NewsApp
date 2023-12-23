package com.aston.news.domain

enum class ArticleSort(name: String) {
    POPULARITY("popularity"), RELEVANCY("relevancy"), NEW("publishedAt")
}