package ru.arzonpay.android.application.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.github.anrwatchdog.ANRWatchDog
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import io.reactivex.plugins.RxJavaPlugins
import ru.arzonpay.android.application.app.di.AppInjector
import ru.arzonpay.android.application.logger.strategies.remote.FirebaseCrashlyticsRemoteLoggingStrategy
import ru.arzonpay.android.application.logger.strategies.remote.RemoteLoggerLoggingStrategy
import ru.arzonpay.android.application.logger.strategies.remote.timber.TimberLoggingStrategy
import ru.arzonpay.android.application.notification.strategy.TransactionPushHandleStrategy
import ru.arzonpay.android.application.notification.type.NotificationTypeData
import ru.arzonpay.android.base.logger.RemoteLogger
import ru.arzonpay.android.base_feature.BuildConfig.BUILD_TYPE
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.f_debug.injector.DebugAppInjector
import ru.arzonpay.android.ui.animation.SlideAnimations
import ru.arzonpay.android.ui.navigation.routes.TransferDetailsFragmentRoute
import ru.arzonpay.android.ui.util.LocaleChanger
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.notification.ui.PushClickProvider
import ru.surfstudio.android.notification.ui.PushEventListener
import ru.surfstudio.android.notification.ui.notification.NOTIFICATION_DATA
import ru.surfstudio.android.utilktx.ktx.ui.activity.ActivityLifecycleListener
import java.util.*

class App : Application() {

    val activeActivityHolder = ActiveActivityHolder()

    override fun attachBaseContext(base: Context) {
        AppInjector.initInjector(this, base)
        val context = LocaleChanger.updateConfiguration(
            base,
            Locale(AppInjector.appComponent.settingsInteractor().getLanguage().code)
        )
        super.attachBaseContext(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        LocaleChanger.updateConfiguration(
            this,
            Locale(AppInjector.appComponent.settingsInteractor().getLanguage().code)
        )
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()

        initAnrWatchDog()
        initLog()
        registerActiveActivityListener()

        RxJavaPlugins.setErrorHandler { Logger.e(it) }
        DebugAppInjector.initInjector(this, activeActivityHolder)

        initFirebaseCrashlytics()
        initFirebasePerformanceMonitor()
        initPushEventListener()
        initRxJava2Debug()
        registerNavigationProviderCallbacks()
        DebugAppInjector.debugInteractor.onCreateApp(R.drawable.ic_launcher)
        initDefaultAnimations()
    }

    private fun initDefaultAnimations() {
        DefaultAnimations.fragment = SlideAnimations
        DefaultAnimations.activity = SlideAnimations
        DefaultAnimations.tab = SlideAnimations
    }

    private fun registerNavigationProviderCallbacks() {
        val provider = AppInjector.appComponent.activityNavigationProvider()
        val callbackProvider = provider as? ActivityNavigationProviderCallbacks ?: return
        registerActivityLifecycleCallbacks(callbackProvider)
    }

    /**
     * отслеживает ANR и отправляет в крашлитикс
     */
    private fun initAnrWatchDog() {
        ANRWatchDog().setReportMainThreadOnly()
            .setANRListener { RemoteLogger.logError(it) }
            .start()
    }

    private fun initLog() {
        Logger.addLoggingStrategy(TimberLoggingStrategy())
        Logger.addLoggingStrategy(RemoteLoggerLoggingStrategy())
        RemoteLogger.addRemoteLoggingStrategy(FirebaseCrashlyticsRemoteLoggingStrategy())
    }

    private fun initRxJava2Debug() {
        RxJava2Debug.enableRxJava2AssemblyTracking(arrayOf(packageName))
    }

    /**
     * Регистрирует слушатель аткивной активити
     */
    private fun registerActiveActivityListener() {
        registerActivityLifecycleCallbacks(
            ActivityLifecycleListener(
                onActivityResumed = { activity ->
                    activeActivityHolder.activity = activity
                },
                onActivityPaused = {
                    activeActivityHolder.clearActivity()
                }
            )
        )
    }

    private fun initFirebaseCrashlytics() {
        FirebaseApp.initializeApp(applicationContext)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isNotDebug())
    }

    private fun initFirebasePerformanceMonitor() {
        FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
    }

    private fun isNotDebug(): Boolean = !BUILD_TYPE.contains("debug")

    private fun initPushEventListener() {
        PushClickProvider.pushEventListener = object : PushEventListener {
            override fun pushDismissListener(context: Context, intent: Intent) {
                //todo
            }

            override fun pushOpenListener(context: Context, intent: Intent) {
                val factory = AppInjector.appComponent.pushStrategyFactory()
                val extras = intent.getSerializableExtra(NOTIFICATION_DATA) as NotificationTypeData
                val strategy = factory.createByData(extras.dataToMap())
                    ?: return

                if (activeActivityHolder.isExist) {
                    if (strategy is TransactionPushHandleStrategy) {
                        val fragmentRoute = TransferDetailsFragmentRoute(
                            strategy.typeData.data?.transaction ?: return
                        )
                        AppInjector.appComponent.commandExecutor().execute(Replace(fragmentRoute))
                    }
                    return
                }

                context.startActivity(strategy.coldStartIntent(context)?.apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

            override fun customActionListener(context: Context, intent: Intent) {
                //todo
            }
        }
    }
}