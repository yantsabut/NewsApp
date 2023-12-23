package com.aston.news.data.sources

import com.aston.news.domain.Source

interface SourcesRepository {

    suspend fun fetch(): List<Source>

}