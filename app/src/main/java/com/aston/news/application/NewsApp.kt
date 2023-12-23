package com.aston.news.application

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.aston.news.di.AppComponent
import com.aston.news.di.DaggerAppComponent
import com.aston.news.di.article.ArticleComponent
import com.aston.news.di.saved.SavedComponent
import com.aston.news.di.sources.SourcesComponent
import com.aston.news.services.ClearWorkerProvider

class NewsApp: Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(ClearWorkerProvider(requireNotNull(appComponent)))
            .build()

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
    }

    fun getAppComponent(): AppComponent {
        return requireNotNull(appComponent)
    }

    fun getArticleComponentFactory(): ArticleComponent.ArticleComponentFactory {
        return requireNotNull(appComponent).getArticleComponentFactory()
    }

    fun  getSavedComponent():SavedComponent {
        return requireNotNull(appComponent).getSavedComponent()
    }

    fun getSourcesComponent(): SourcesComponent {
        return requireNotNull(appComponent).getSourcesComponent()
    }


}