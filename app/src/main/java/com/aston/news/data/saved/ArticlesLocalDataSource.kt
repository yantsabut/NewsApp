package com.aston.news.data.saved

import com.aston.news.domain.Article
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

interface ArticlesLocalDataSource {

    suspend fun insert(article: Article)

    fun observeAll(): Flow<List<Article>>

    suspend fun clearByDate(date: Calendar)

    suspend fun isExist(article: Article): Boolean

}