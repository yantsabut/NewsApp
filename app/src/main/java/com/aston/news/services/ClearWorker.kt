package com.aston.news.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aston.news.data.saved.ArticlesLocalDataSource
import java.util.Calendar
import javax.inject.Inject

class ClearWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repository: ArticlesLocalDataSource
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val date = Calendar.getInstance().also {
            it.add(Calendar.WEEK_OF_YEAR, -2)
        }
        repository.clearByDate(date)
        return Result.success()
    }

}