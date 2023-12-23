package com.aston.news.presentation.headlines

import com.aston.news.core.network.NetworkUtils
import com.aston.news.domain.Article
import com.aston.news.domain.Categories
import com.aston.news.presentation.adapters.headlines.HeadlineItem

fun Article.asItem(category: Categories): HeadlineItem {
    return HeadlineItem(
        src = urlToImage.orEmpty(),
        title = title,
        sourceSrc = NetworkUtils.getSourceIconUrl(url),
        sourceName = source.name,
        category = category,
        article = this
    )
}

fun List<Article>.asItems(category: Categories): List<HeadlineItem> {
    return this.map { it.asItem(category) }
}