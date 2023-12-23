package com.aston.news.di.headlines

import com.aston.news.data.headlines.HeadlinesRepository
import com.aston.news.data.headlines.HeadlinesRepositoryImp
import dagger.Module
import dagger.Provides

@Module
object HeadlinesModule {

    @Provides
    fun provideHeadlineRepository(impl: HeadlinesRepositoryImp): HeadlinesRepository = impl

}