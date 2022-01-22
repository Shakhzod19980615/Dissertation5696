package ru.arzonpay.android.application.notification


import dagger.Component
import ru.arzonpay.android.application.app.di.AppComponent
import ru.arzonpay.android.i_push_notification.storage.DeviceStorage
import javax.inject.Scope

@PerService
@Component(dependencies = [AppComponent::class])
interface FirebaseServiceComponent {

    fun pushStorage(): DeviceStorage

    fun inject(s: MessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService