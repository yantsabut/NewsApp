package com.aston.news.base

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    protected val onDestroyCompositeDisposable = CompositeDisposable()
    protected val onDetachCompositeDisposable = CompositeDisposable()

    protected fun Disposable.disposeOnDestroy() :Disposable{
        onDestroyCompositeDisposable.add(this)
        return this
    }

    protected fun Disposable.disposeOnDetach() :Disposable{
        onDetachCompositeDisposable.add(this)
        return this
    }

    @CallSuper
    override fun detachView(view: View) {
        onDetachCompositeDisposable.clear()
        super.detachView(view)
    }

    @CallSuper
    override fun onDestroy() {
        onDestroyCompositeDisposable.clear()
        super.onDestroy()
    }
}
