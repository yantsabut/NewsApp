package com.aston.news.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.aston.news.di.AppComponent
import ru.itmo.notes.di.clearWorker.DaggerWorkerComponent

class ClearWorkerProvider(
    private val appComponent: AppComponent
): WorkerFactory() {

    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        val component = DaggerWorkerComponent.builder().context(appContext).parameters(workerParameters).appComponent(appComponent).create()

        return when (workerClassName) {
            ClearWorker::class.qualifiedName -> component.clearWorker()
            else -> null
        }
    }
}