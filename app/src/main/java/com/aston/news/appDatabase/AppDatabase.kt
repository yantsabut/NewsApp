package com.aston.news.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aston.news.data.saved.ArticleRoom
import com.aston.news.data.saved.ArticlesDao
import ru.itmo.notes.data.converters.CalendarConverter

@Database(
    version = 1,
    entities = [
        ArticleRoom::class
    ]
)
@TypeConverters(CalendarConverter::class)
abstract class AppDatabase(): RoomDatabase() {

    abstract fun getArticlesDao(): ArticlesDao

}