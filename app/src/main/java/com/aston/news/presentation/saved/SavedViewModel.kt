package com.aston.news.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aston.news.data.saved.ArticlesLocalDataSource
import com.aston.news.domain.Categories
import com.aston.news.presentation.adapters.headlines.HeadlineItem
import com.aston.news.presentation.headlines.asItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedViewModel @Inject constructor(
    private val localDataSource: ArticlesLocalDataSource
): ViewModel() {

    private val state = MutableStateFlow(ViewState())
    fun uiState() = state.asStateFlow()

    init {
        viewModelScope.launch {
            localDataSource
                .observeAll()
                .collect{
                    val items = it.map { it.asItem(Categories.GENERAL) }
                    state.update { it.copy(items = items) }
                }
        }
    }

}

data class ViewState(
    val items: List<HeadlineItem> = emptyList()
)