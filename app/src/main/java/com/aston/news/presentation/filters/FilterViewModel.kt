package com.aston.news.presentation.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aston.news.base.BaseViewModel
import com.aston.news.data.filters.FiltersRepository
import com.aston.news.domain.Filter
import javax.inject.Inject

class FilterViewModel @Inject constructor(
    private val filtersRepository: FiltersRepository
):BaseViewModel<FilterIntent>() {

    private var viewState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = viewState

    private var date: Pair<Long, Long>? = null

    init {
        date = filtersRepository.get().date
    }

    override fun onTriggerEvent(eventType: FilterIntent) {
        when (eventType) {
            is FilterIntent.FetchData -> { fetchData() }
            is FilterIntent.SetDate -> {
                date = eventType.date
                fetchData()
            }
            is FilterIntent.SetFilter -> {
                val filter = eventType.filter.copy(date = date)
                filtersRepository.save(filter)
            }
        }
    }

    private fun fetchData() {
        val filter = filtersRepository.get().copy(date = date)
        viewState.value = ViewState(filter = filter)
    }

}

data class ViewState(
    val filter: Filter
)