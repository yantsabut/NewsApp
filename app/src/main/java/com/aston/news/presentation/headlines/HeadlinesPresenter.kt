package com.aston.news.presentation.headlines

import com.aston.news.base.BasePresenter
import com.aston.news.data.filters.FiltersRepository
import com.aston.news.data.headlines.HeadlinesRepository
import com.aston.news.domain.Article
import com.aston.news.domain.Categories
import com.aston.news.domain.Filter
import com.aston.news.extension.wrapInAsyncTask
import com.aston.news.presentation.adapters.headlines.HeadlineItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class HeadlinesPresenter @Inject constructor(
    private val headlinesRepository: HeadlinesRepository,
    private val filtersRepository: FiltersRepository
): BasePresenter<HeadlinesViewContract>() {

    private val items = mutableListOf<HeadlineItem>()

    private var category: Categories = Categories.GENERAL
    private var query: String = ""
    private val filters: Filter
        get() = filtersRepository.get()
    private var source: String? = null

    fun getFiltersCount(): Int {
        val filter = filtersRepository.get()
        var count = 0
        count += if (filter.date != null) 1 else 0
        count += if (filter.sort!= null) 1 else 0

        return count
    }

    fun fetchDataObserver(page: Int): Observable<List<HeadlineItem>> {
        Completable.create { viewState.showContentProgressBar() }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()

        return getArticleObservable(page)
            .toObservable()
            .map { it.asItems(category) }
            .doOnError { viewState.hideContentProgressBar() }
            .doOnNext { items += it }
            .doOnNext { viewState.hideContentProgressBar() }
            .map { items.filter { it.category == category }.toList() }
    }

    private fun getArticleObservable(page: Int = 1): Single<List<Article>> {
        val filter = filters

        return headlinesRepository.fetchArticles(
            type = category,
            page = page,
            query = query,
            sortBy = filter.sort,
            from = filter.date?.first,
            to = filter.date?.second,
            lang = filter.language,
            source = source
        )
            .wrapInAsyncTask()
    }

    fun fetchData() {
        getArticleObservable()
            .doOnSubscribe { viewState.showContentProgressBar() }
            .map { it.asItems(category) }
            .doOnSuccess { items += it }
            .subscribe({
                viewState.hideContentProgressBar()
                viewState.setData(it)
            }, { viewState.hideContentProgressBar() })
            .disposeOnDestroy()
    }

    fun setFilter(type: Categories) {
        category = type
        viewState.setData(items.filter { it.category == category })
        fetchData()
    }

    fun setQuery(text: String) {
        query = text
        viewState.setData(items.filter { it.title.contains(query)})
        fetchData()
    }

    fun setSource(source: String) {
        this.source = source
    }

}