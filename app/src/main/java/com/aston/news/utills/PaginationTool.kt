package com.aston.news.utills

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PaginationTool<T> {

    private var recyclerView: RecyclerView? = null
    private lateinit var pagingListener: PagingListener
    private var limit = 0
    private var emptyListCount = 0
    private var retryCount = 0
    private var emptyListCountPlusToOffset = false

    val pagingObservable: Observable<T>
        get() {
            val startNumberOfRetryAttempt = 0
            return getScrollObservable(recyclerView, limit, emptyListCount)
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.from(BackgroundExecutor.safeBackgroundExecutor))
                .switchMap { offset: Int ->
                    getPagingObservable(
                        pagingListener,
                        pagingListener.onNextPage(offset),
                        startNumberOfRetryAttempt,
                        offset,
                        retryCount
                    )
                }
        }

    private fun getScrollObservable(recyclerView: RecyclerView?, limit: Int, emptyListCount: Int): Observable<Int> {
        return Observable.create { subscriber: ObservableEmitter<Int> ->
            val sl: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!subscriber.isDisposed) {
                        val position = getLastVisibleItemPosition(recyclerView)
                        val updatePosition = recyclerView.adapter!!.itemCount - 1
                        if (position >= updatePosition) {
                            val offset =
                                if (emptyListCountPlusToOffset) recyclerView.adapter!!.itemCount else recyclerView.adapter!!.itemCount - emptyListCount
                            val page = offset / limit + 1

                            subscriber.onNext(page.takeIf { it > 0 } ?: 1)
                        }

                        if (position < 0) {
                            subscriber.onNext(1)
                        }
                    }
                }
            }
            recyclerView!!.addOnScrollListener(sl)
            subscriber.setCancellable { recyclerView.removeOnScrollListener(sl) }

            if (recyclerView.adapter!!.itemCount == emptyListCount) {
                val offset =
                    if (emptyListCountPlusToOffset) recyclerView.adapter!!.itemCount else recyclerView.adapter!!
                        .itemCount - emptyListCount
                subscriber.onNext(offset.takeIf { it > 0 } ?: 1)
            }
        }
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        return linearLayoutManager!!.findLastVisibleItemPosition()
    }

    private fun getPagingObservable(
        listener: PagingListener,
        observable: Observable<T>,
        numberOfAttemptToRetry: Int,
        offset: Int,
        retryCount: Int
    ): Observable<T> {
        return observable.onErrorResumeNext { throwable: Throwable? ->
            // retry to load new data portion if error occurred
            if (numberOfAttemptToRetry < retryCount) {
                val attemptToRetryInc = numberOfAttemptToRetry + 1
                return@onErrorResumeNext getPagingObservable(listener, listener.onNextPage(offset), attemptToRetryInc, offset, retryCount)
            } else {
                return@onErrorResumeNext Observable.error(throwable)
            }
        }
    }

    class Builder<T> (
        private val recyclerView: RecyclerView,
        private val pagingListener: PagingListener
    ) {

        private var limit = DEFAULT_LIMIT
        private var emptyListCount = EMPTY_LIST_ITEMS_COUNT
        private var retryCount = MAX_ATTEMPTS_TO_RETRY_LOADING
        private var emptyListCountPlusToOffset = false

        init {
            if (recyclerView.adapter == null) {
                throw PagingException("null recyclerView adapter")
            }
        }

        fun setLimit(limit: Int): Builder<T> {
            if (limit <= 0) {
                throw PagingException("limit must be greater then 0")
            }
            this.limit = limit
            return this
        }

        fun build(): PaginationTool<T> {
            val paginationTool: PaginationTool<T> = PaginationTool()
            paginationTool.recyclerView = recyclerView
            paginationTool.pagingListener = pagingListener
            paginationTool.limit = limit
            paginationTool.emptyListCount = emptyListCount
            paginationTool.retryCount = retryCount
            paginationTool.emptyListCountPlusToOffset = emptyListCountPlusToOffset
            return paginationTool
        }
    }

    companion object {
        // for first start of items loading then on RecyclerView there are not items and no scrolling
        private const val EMPTY_LIST_ITEMS_COUNT = 0

        // default limit for requests
        private const val DEFAULT_LIMIT = 50

        // default max attempts to retry loading request
        private const val MAX_ATTEMPTS_TO_RETRY_LOADING = 0

        fun <T> buildPagingObservable(recyclerView: RecyclerView, pagingListener: PagingListener): Builder<T> {
            return Builder(recyclerView, pagingListener)
        }
    }
}