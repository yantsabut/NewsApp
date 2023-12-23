package com.aston.news.di.saved

import com.aston.news.presentation.saved.SavedViewModel
import dagger.Subcomponent

@Subcomponent
interface SavedComponent {

    fun getViewModel(): SavedViewModel

}