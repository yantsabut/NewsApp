package com.aston.news.presentation.filters

import com.aston.news.base.BaseIntent
import com.aston.news.domain.Filter

sealed class FilterIntent: BaseIntent() {

    data object FetchData: FilterIntent()

    data class SetDate(val date: Pair<Long, Long>?): FilterIntent()

    data class SetFilter(val filter: Filter): FilterIntent()

}