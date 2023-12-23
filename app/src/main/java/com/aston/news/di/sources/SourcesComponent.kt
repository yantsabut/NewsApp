package com.aston.news.di.sources

import com.aston.news.presentation.sources.SourcesViewModel
import dagger.Subcomponent

@Subcomponent
interface SourcesComponent {

    fun getViewModel(): SourcesViewModel

}