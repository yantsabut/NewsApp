package com.aston.news.presentation.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aston.news.data.saved.ArticlesLocalDataSource
import com.aston.news.domain.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private val article: Article,
    private val savedRepository: ArticlesLocalDataSource
): ViewModel() {


    private val state = MutableStateFlow(
        DataState(
            title = article.title,
            date = formatDate(article.publishedAt),
            source = article.source.name,
            description = article.description,
            src = article.urlToImage,
            url = article.url
        )
    )

    init {
        viewModelScope.launch {
            state.update { it.copy(isSaved = savedRepository.isExist(article)) }
        }
    }

    fun uiState() = state.asStateFlow()

    private fun formatDate(publishedAt: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(publishedAt)
        return date?.let { SimpleDateFormat("LLL dd, yyyy | HH:mm aa").format(it) } ?: ""
    }

    fun save() {
        viewModelScope.launch {
            savedRepository.insert(article)
        }
    }

}

data class DataState(
    val title: String,
    val date: String,
    val source: String,
    val description: String?,
    val src: String?,
    val url: String,
    val isSaved: Boolean = false
)