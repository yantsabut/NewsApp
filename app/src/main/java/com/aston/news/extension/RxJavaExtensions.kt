package com.aston.news.extension

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Observable.defer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

fun <T> Observable<T>.wrapInAsyncTask(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.andThenDo(next: CompletableSource): Completable {
    return andThen(Completable.defer { next })
}

fun Completable.andThenDo(next: () -> CompletableSource): Completable {
    return andThen(Completable.defer(next))
}

fun <T> Completable.andThenSingle(next: Single<T>): Single<T> {
    return andThen(Single.defer { next })
}


fun Completable.wrapInAsyncTask(): Completable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.wrapInAsyncTask(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.wrapInAsyncTask(): Flowable<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable: Throwable ->
            Observable.error<T>(throwable)
                .observeOn(AndroidSchedulers.mainThread())
        }
}

fun <T> Maybe<T>.observeOnMainThread(): Maybe<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable: Throwable ->
            Maybe.error<T>(throwable).observeOn(AndroidSchedulers.mainThread())
        }
}

fun <T> Single<T>.observeOnMainThread(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread()).onErrorResumeNext {
        Single.error<T>(it).observeOn(AndroidSchedulers.mainThread())
    }
}

fun Completable.observeOnMainThread(): Completable {
    return this.observeOn(AndroidSchedulers.mainThread()).onErrorResumeNext {
        Completable.error(it).observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Observable<T>.retry(predicate: (Throwable) -> Boolean, maxRetry: Int, delayBeforeRetry: Long): Observable<T> {
    return retryWhen { observable ->
        Observable.zip<Throwable, Int, Int>(
            observable.map { throwable -> if (predicate(throwable)) throwable else throw throwable },
            Observable.range(1, maxRetry),
            BiFunction { throwable, countRetry ->
                if (countRetry == maxRetry)
                    throw  throwable
                countRetry
            }
        ).delay(delayBeforeRetry, TimeUnit.SECONDS)
    }
}

fun <T> mergeOneError(observables: List<Observable<T>>): Observable<T> {
    val firstErrorFlag = AtomicBoolean()

    val newObservables = observables.map {
        it.resumeOnlyOneError(firstErrorFlag)
    }

    return Observable.merge(newObservables)
}

fun <T> mergeOneError(singles: List<Single<T>>): Flowable<T> {
    val firstErrorFlag = AtomicBoolean()

    val newObservables = singles.map {
        it.resumeOnlyOneError(firstErrorFlag)
    }

    return Single.merge(newObservables)
}

fun <T> Observable<T>.resumeOnlyOneError(firstErrorFlag: AtomicBoolean): Observable<T> {
    return this.onErrorResumeNext { error: Throwable ->
        if (firstErrorFlag.compareAndSet(false, true))
            Observable.error<T>(error)
        else
            Observable.empty<T>()
    }
}

fun <T> Single<T>.resumeOnlyOneError(firstErrorFlag: AtomicBoolean): Single<T> {
    return this.onErrorResumeNext { error: Throwable ->
        if (firstErrorFlag.compareAndSet(false, true))
            Single.error<T>(error)
        else
            Single.never<T>()
    }
}

fun <T> Observable<T>.toCompletable(): Completable {
    return firstOrError().toCompletable()
}

fun <T> Observable<T>.withPrevious(): Observable<Pair<T?, T>> {
    return this
        .scan(listOf<T>()) { res, value ->
            res.toMutableList().apply { add(value) }.takeLast(2)
        }
        .filter { it.isNotEmpty() }
        .map { Pair(if (it.size > 1) it.first() else null, it.last()) }
}

fun <T> mergeFirstErrorOnly(vararg sources: Observable<T>): Observable<T> {
    return defer {
        val once = AtomicBoolean()
        val errorSubject = PublishSubject.create<Void>()
        val newSources = sources.map {
            it.onErrorResumeNext(Function { e ->
                if (once.compareAndSet(false, true)) {
                    errorSubject.onError(e)
                }
                return@Function Observable.empty()
            })
        }

        Observable.merge(newSources).takeUntil(errorSubject).observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Observable<T>.retryAfterDelayWithoutError(times: Long = -1, delay: Long, timeUnit: TimeUnit): Observable<T> {
    return this.retryWhen {
        val ob = if (times != -1L) it.take(times) else it
        ob.delay(delay, timeUnit)
    }
}

fun <T> Single<T>.retryAfterDelayWithoutError(times: Long = -1, delay: Long, timeUnit: TimeUnit): Single<T> {
    return this.retryWhen {
        val ob = if (times != -1L) it.take(times) else it
        ob.delay(delay, timeUnit)
    }
}

fun Completable.retryAfterDelayWithoutError(times: Long = -1, delay: Long, timeUnit: TimeUnit): Completable {
    return this.retryWhen {
        val ob = if (times != -1L) it.take(times) else it
        ob.delay(delay, timeUnit)
    }
}

fun Completable.retryAfterDelayWithError(times: Long = -1L, delay: Long, timeUnit: TimeUnit): Completable {
    var retryCount = 0
    return this.retryWhen { errorFlowable ->
        errorFlowable.flatMap { throwable ->
            if (++retryCount < times || times == -1L) {
                Flowable.timer(delay, timeUnit)
            } else {
                Flowable.error(throwable)
            }
        }
    }
}

val Disposable?.isNotDisposed: Boolean
    get() {
        val disposable = this
        return disposable != null && !disposable.isDisposed
    }

fun <T> Observable<T>.toRefCountObservable(
    doOnSubscribe: ((Disposable) -> Unit)? = null,
    doOnDispose: (() -> Unit)? = null
)
        : Observable<T> {

    val onSubscribe = if (doOnSubscribe != null) {
        this.doOnSubscribe(doOnSubscribe)
    } else {
        this
    }

    val onDispose = if (doOnDispose != null) {
        onSubscribe.doOnDispose(doOnDispose)
    } else {
        onSubscribe
    }

    return onDispose.replay(1)
        .refCount()
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}