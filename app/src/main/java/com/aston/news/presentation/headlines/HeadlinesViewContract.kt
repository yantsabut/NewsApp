package com.aston.news.presentation.headlines

import com.aston.news.presentation.adapters.headlines.HeadlineItem
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface HeadlinesViewContract: MvpView {

    fun setData(items: List<HeadlineItem>)

    fun showContentProgressBar()

    fun hideContentProgressBar()

}