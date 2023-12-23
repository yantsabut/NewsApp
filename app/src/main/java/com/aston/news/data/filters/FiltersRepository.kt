package com.aston.news.data.filters

import com.aston.news.domain.Filter

interface FiltersRepository {

    fun save(filter: Filter)

    fun get(): Filter

}