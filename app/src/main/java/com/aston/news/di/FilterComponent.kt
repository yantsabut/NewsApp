package com.aston.news.di

import com.aston.news.presentation.filters.FilterViewModel
import dagger.Subcomponent

@Subcomponent
interface FilterComponent {

    fun getViewModel(): FilterViewModel

}