package com.aston.news.data.sources

import com.aston.news.domain.Source
import javax.inject.Inject

class SourcesRepositoryImpl @Inject constructor(
    private val api: SourcesAPI
): SourcesRepository {

    override suspend fun fetch(): List<Source> {
        return api.fetch().sources
    }
}