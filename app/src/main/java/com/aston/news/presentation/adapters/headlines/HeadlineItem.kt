package com.aston.news.presentation.adapters.headlines

import com.aston.news.core.ui.ListItem
import com.aston.news.domain.Article
import com.aston.news.domain.Categories

data class HeadlineItem(
    val src: String,
    val title: String,
    val sourceSrc: String?,
    val sourceName: String,
    val category: Categories,
    val article: Article
) : ListItem {

    override val id: Any = title

}