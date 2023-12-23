package ru.itmo.notes.di.clearWorker

import android.content.Context
import androidx.work.WorkerParameters
import com.aston.news.di.AppComponent
import com.aston.news.di.DatabaseModule
import com.aston.news.di.RepositoryModule
import com.aston.news.services.ClearWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope


@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Worker

@Worker
@Component(
    dependencies = [AppComponent::class],
    modules = [
        DatabaseModule::class,
        RepositoryModule::class
    ]
)
interface WorkerComponent {

    fun clearWorker(): ClearWorker

    @Component.Builder
    interface WorkerComponentBuilder {

        fun context(
            @BindsInstance context: Context,
        ): WorkerComponentBuilder

        fun parameters(
            @BindsInstance workerParameters: WorkerParameters,
        ): WorkerComponentBuilder

        fun appComponent(
            appComponent: AppComponent
        ): WorkerComponentBuilder

        fun create(): WorkerComponent
    }
}