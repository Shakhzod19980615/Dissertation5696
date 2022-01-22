package ru.arzonpay.android.application.app.di

import android.content.Context
import android.content.SharedPreferences
import ru.arzonpay.android.i_auth.AuthInteractor
import ru.arzonpay.android.i_auth.sms.SmsRetrieverService
import ru.arzonpay.android.i_config.ConfigInteractor
import ru.arzonpay.android.i_initialization.InitializeAppInteractor
import ru.arzonpay.android.i_network.network.BaseUrl
import ru.arzonpay.android.i_payment.PaymentInteractor
import ru.arzonpay.android.i_push_notification.storage.DeviceInteractor
import ru.arzonpay.android.i_push_notification.storage.DeviceStorage
import ru.arzonpay.android.i_rate.RateInteractor
import ru.arzonpay.android.i_session.SessionChangedInteractor
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.arzonpay.android.ui.mvi.navigation.IntentChecker
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

/**
 * Интерфейс, объединяющий в себе все зависимости в скоупе [PerApplication]
 * Следует использовать в компоненте Application и других компонентах более высоких уровней,
 * зависящих от него.
 */
interface AppProxyDependencies {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangedInteractor(): SessionChangedInteractor
    fun schedulerProvider(): SchedulersProvider
    fun resourceProvider(): ResourceProvider
    fun globalNavigator(): GlobalNavigator
    fun intentChecker(): IntentChecker

    fun commandExecutor(): AppCommandExecutor
    fun activityNavigationProvider(): ActivityNavigationProvider
    fun screenResultObserver(): ScreenResultObserver
    fun screenResultEmitter(): ScreenResultEmitter

    fun analyticService(): DefaultAnalyticService
    fun smsRetrieverService(): SmsRetrieverService

    fun fcmStorage(): DeviceStorage
    fun pushHandler(): PushHandler
    fun pushStrategyFactory(): AbstractPushHandleStrategyFactory

    fun baseUrl(): BaseUrl

    @Named(NO_BACKUP_SHARED_PREF)
    fun sharedPreferences(): SharedPreferences

    fun authInteractor(): AuthInteractor
    fun paymentInteractor(): PaymentInteractor
    fun configInteractor(): ConfigInteractor
    fun settingsInteractor(): SettingsInteractor
    fun deviceInteractor(): DeviceInteractor
    fun rateInteractor(): RateInteractor
}