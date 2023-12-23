package com.aston.news.di.article

import com.aston.news.domain.Article
import com.aston.news.presentation.article.ArticleViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface ArticleComponent {

    fun getViewModel(): ArticleViewModel

    @Subcomponent.Factory
    interface ArticleComponentFactory {

        fun create(@BindsInstance article: Article): ArticleComponent

    }

}