package com.aston.news.data.saved

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

@Dao
interface ArticlesDao {

    @Insert
    suspend fun insert(article: ArticleRoom)

    @Query("SELECT * FROM articles")
    fun observeAll(): Flow<List<ArticleRoom>>

    @Query("DELETE FROM articles WHERE date <= :date")
    suspend fun clearByDate(date: Calendar)

    @Query("SELECT * FROM articles WHERE title = :title")
    suspend fun getByTitle(title: String): ArticleRoom?

}