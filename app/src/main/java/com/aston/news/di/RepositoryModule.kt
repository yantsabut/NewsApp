package com.aston.news.di

import com.aston.news.data.filters.FiltersRepository
import com.aston.news.data.filters.FiltersRepositoryImpl
import com.aston.news.data.saved.ArticlesLocalDataSource
import com.aston.news.data.saved.ArticlesLocalDataSourceImpl
import com.aston.news.data.sources.SourcesRepository
import com.aston.news.data.sources.SourcesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @Provides
    fun provideFiltersRepository(impl: FiltersRepositoryImpl): FiltersRepository = impl

    @Provides
    fun provideArticlesLocalDataSource(impl: ArticlesLocalDataSourceImpl): ArticlesLocalDataSource = impl

    @Provides
    fun provideSourcesRepository(impl: SourcesRepositoryImpl): SourcesRepository = impl

}