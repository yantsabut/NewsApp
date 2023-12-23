package com.aston.news.data.filters

import android.content.Context
import com.aston.news.domain.ArticleLanguage
import com.aston.news.domain.Filter
import com.google.gson.Gson
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    context: Context
): FiltersRepository {

    private val pref = context.getSharedPreferences(FILTER_NAME, Context.MODE_PRIVATE)

    override fun save(filter: Filter) {
        with (pref.edit()) {
            putString(FILTER_KEY, Gson().toJson(filter))
            apply()
        }
    }

    override fun get(): Filter {
        return pref.getString(FILTER_KEY, null)?.let {
            Gson().fromJson(it, Filter::class.java)
        } ?: Filter(language = ArticleLanguage.ENGLISH)
    }

    companion object {
        const val FILTER_NAME = "filters"
        const val FILTER_KEY = "filters"
    }
}