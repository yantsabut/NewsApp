package com.aston.news.presentation.sources

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aston.news.data.filters.FiltersRepository
import com.aston.news.data.sources.SourcesRepository
import com.aston.news.domain.Source
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourcesViewModel @Inject constructor(
    private val repository: SourcesRepository,
    private val filtersRepository: FiltersRepository
): ViewModel() {

    private val state = MutableStateFlow(ViewState())
    fun uiState() = state.asStateFlow()
    private val items = mutableListOf<Source>()

    private var query: String = ""

    init {
        fetchData()
        getFiltersCount()
    }

    fun getFiltersCount(): Int {
        val filter = filtersRepository.get()
        var count = 0
        count += if (filter.date != null) 1 else 0
        count += if (filter.sort!= null) 1 else 0

        return count
    }

    fun fetchData() {
        viewModelScope.launch {
            state.update { it.copy(isLoading = true) }
            val result = repository.fetch()
            items.clear()
            items += result
            state.update { it.copy(items = items.filter { it.name.lowercase().contains(query.lowercase()) }, isLoading = false) }
        }
    }

    fun setQuery(q: String) {
        Log.d("TAG", "$q")
        query = q
        state.update { it.copy(items = items.filter { it.name.lowercase().contains(query.lowercase()) }) }
    }

}

data class ViewState(
    val items: List<Source> = emptyList(),
    val isLoading: Boolean = false,
    val countFilters: Int = 0
)