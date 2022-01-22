package ru.arzonpay.android.application.app.di

import dagger.Component
import ru.arzonpay.android.application.analytics.di.AnalyticsModule
import ru.arzonpay.android.application.auth.di.AuthModule
import ru.arzonpay.android.application.cache.di.CacheModule
import ru.arzonpay.android.application.config.di.ConfigModule
import ru.arzonpay.android.application.migration.di.MigrationModule
import ru.arzonpay.android.application.network.di.EtagModule
import ru.arzonpay.android.application.network.di.NetworkModule
import ru.arzonpay.android.application.network.di.OkHttpModule
import ru.arzonpay.android.application.notification.MessagingService
import ru.arzonpay.android.application.notification.NotificationModule
import ru.arzonpay.android.application.notification.PushModule
import ru.arzonpay.android.application.payment.di.PaymentModule
import ru.arzonpay.android.application.storage.di.SharedPrefModule
import ru.arzonpay.android.ui.navigation.di.NavigationModule
import ru.surfstudio.android.dagger.scope.PerApplication

@PerApplication
@Component(
    modules = [
        AnalyticsModule::class,
        AppModule::class,
        MigrationModule::class,
        SharedPrefModule::class,
        AuthModule::class,
        CacheModule::class,
        ConfigModule::class,
        EtagModule::class,
        NetworkModule::class,
        OkHttpModule::class,
        PushModule::class,
        NotificationModule::class,
        NavigationModule::class,
        PaymentModule::class
    ]
)
interface AppComponent : AppProxyDependencies {
    fun inject(to: MessagingService)
}