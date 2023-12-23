package com.aston.news.utills

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object BackgroundExecutor {
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = CPU_COUNT + 1
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    private const val KEEP_ALIVE = 1
    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(128)

    val safeBackgroundExecutor: Executor = ThreadPoolExecutor(
        CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE.toLong(),
        TimeUnit.SECONDS, sPoolWorkQueue
    )

    fun <T> createSafeBackgroundObservable(f: ObservableOnSubscribe<T>): Observable<T> {
        return Observable.create(f).subscribeOn(Schedulers.from(safeBackgroundExecutor))
    }
}