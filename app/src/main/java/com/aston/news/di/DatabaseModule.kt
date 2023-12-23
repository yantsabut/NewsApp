package com.aston.news.di

import android.content.Context
import androidx.room.Room
import com.aston.news.appDatabase.AppDatabase
import com.aston.news.data.saved.ArticlesDao
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideArticleDao(appDatabase: AppDatabase): ArticlesDao = appDatabase.getArticlesDao()

}