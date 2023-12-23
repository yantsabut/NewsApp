package com.aston.news.data.saved

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "articles")
data class ArticleRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val content: String?,
    val publishedAt: String,
    val date: Calendar = Calendar.getInstance()
)