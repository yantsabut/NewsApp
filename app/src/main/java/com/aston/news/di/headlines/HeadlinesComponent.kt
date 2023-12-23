package com.aston.news.di.headlines

import com.aston.news.presentation.headlines.HeadlinesFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        HeadlinesModule::class
    ]
)
interface HeadlinesComponent {

    fun inject(fragment: HeadlinesFragment)

}