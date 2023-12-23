package com.aston.news.data.headlines

import com.aston.news.data.headlines.remote.HeadlinesAPI
import com.aston.news.domain.Article
import com.aston.news.domain.ArticleLanguage
import com.aston.news.domain.ArticleSort
import com.aston.news.domain.Categories
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HeadlinesRepositoryImp @Inject constructor(
    private val api: HeadlinesAPI
) : HeadlinesRepository {

    override fun fetchArticles(
        type: Categories,
        page: Int,
        query: String,
        sortBy: ArticleSort?,
        from: Long?,
        to: Long?,
        lang: ArticleLanguage?,
        source: String?
    ): Single<List<Article>> {

        val result =  if (sortBy != null || from != null) {
            api.fetchEverything(
                query = query,
                from = if (from != null) SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(from)  else "",
                to = if (to != null) SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(to)  else "",
                lang = lang?.shortName ?: ArticleLanguage.ENGLISH.shortName,
                sort = sortBy?.name ?: ArticleSort.NEW.name,
                page = page,
                sources = source.orEmpty())
        } else if (source == null) {
            api.fetchTopHeadlinesByCategory(
                query = query,
                type = type,
                page = page,
            )
        } else {
            api.fetchTopHeadlinesBySource(
                query = query,
                source = source,
                page = page
            )
        }

        return result.map { it.articles }
    }
}