package com.aston.news.data.saved

import com.aston.news.domain.Article
import com.aston.news.domain.Source
import com.google.gson.Gson


fun Article.asRoom(): ArticleRoom {
    return  ArticleRoom(
    source = Gson().toJson(source),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    content = content,
    publishedAt = publishedAt,
    )
}

fun ArticleRoom.asDomain(): Article {
    return  Article(
        source = Gson().fromJson(source, Source::class.java),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        content = content,
        publishedAt = publishedAt,
    )
}

fun List<ArticleRoom>.asDomain() = this.map { it.asDomain() }