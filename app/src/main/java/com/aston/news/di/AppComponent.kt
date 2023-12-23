package com.aston.news.di

import android.content.Context
import com.aston.news.di.article.ArticleComponent
import com.aston.news.di.headlines.GsonConverterProvider
import com.aston.news.di.headlines.HeadlinesComponent
import com.aston.news.di.saved.SavedComponent
import com.aston.news.di.sources.SourcesComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HTTPClientModule::class,
        GsonConverterProvider::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ApiModule::class
    ]
)
interface AppComponent {

    fun getHeadlinesComponent(): HeadlinesComponent

    fun getFilterComponent(): FilterComponent

    fun getArticleComponentFactory(): ArticleComponent.ArticleComponentFactory

    fun getSavedComponent(): SavedComponent

    fun getSourcesComponent(): SourcesComponent

    @Component.Builder
    interface AppComponentBuilder {

        fun application(@BindsInstance application: Context): AppComponentBuilder

        fun build(): AppComponent
    }

}