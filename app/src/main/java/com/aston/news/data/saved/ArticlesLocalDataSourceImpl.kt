package com.aston.news.data.saved

import com.aston.news.domain.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class ArticlesLocalDataSourceImpl @Inject constructor(
    private val dao: ArticlesDao
) : ArticlesLocalDataSource {

    override suspend fun insert(article: Article) {
        dao.getByTitle(article.title) ?: run { dao.insert(article.asRoom()) }
    }

    override fun observeAll(): Flow<List<Article>> {
        return dao.observeAll().map { it.asDomain() }
    }

    override suspend fun clearByDate(date: Calendar) {
        dao.clearByDate(date)
    }

    override suspend fun isExist(article: Article): Boolean {
        return dao.getByTitle(article.title) != null
    }
}