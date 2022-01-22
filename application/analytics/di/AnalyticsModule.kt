package ru.arzonpay.android.application.analytics.di

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.ui.analytics.base.configDefaultFirebaseAnalyticsActions
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
object AnalyticsModule {

    @Provides
    @PerApplication
    @SuppressLint("MissingPermission")
    internal fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @PerApplication
    internal fun provideDefaultAnalyticService(
        firebaseAnalytics: FirebaseAnalytics
    ): DefaultAnalyticService {
        //TODO добавить настройки аналитики для хуавеи
        return DefaultAnalyticService()
            .configDefaultFirebaseAnalyticsActions(firebaseAnalytics)
    }
}
