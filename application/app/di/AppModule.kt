package ru.arzonpay.android.application.app.di

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.ui.resource.LocaleContextProvider
import ru.arzonpay.android.ui.resource.LocaledResourceProvider
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProviderImpl
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@Module
class AppModule(
    private val app: Application,
    private val context: Context,
    private val activeActivityHolder: ActiveActivityHolder
) {

    @Provides
    @PerApplication
    internal fun provideActiveActivityHolder(): ActiveActivityHolder = activeActivityHolder

    @Provides
    @PerApplication
    internal fun provideContext(): Context = context

    @Provides
    @PerApplication
    internal fun provideApp(): Application = app

    @Provides
    @PerApplication
    internal fun provideResourceProvider(contextProvider: LocaleContextProvider): ResourceProvider {
        return LocaledResourceProvider(contextProvider)
    }

    @Provides
    @PerApplication
    internal fun provideGlobalNavigator(
        context: Context,
        activityHolder: ActiveActivityHolder
    ): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSchedulerProvider(): SchedulersProvider = SchedulersProviderImpl()

    @Provides
    @PerApplication
    internal fun provideConnectionQualityProvider(context: Context): ConnectionProvider {
        return ConnectionProvider(context)
    }

    @Provides
    @PerApplication
    internal fun providePermissionManager(
        activeActivityHolder: ActiveActivityHolder,
        appCommandExecutorWithResult: AppCommandExecutorWithResult,
        @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences,
        screenResultObserver: ScreenResultObserver
    ): PermissionManager {
        return PermissionManager(
            activeActivityHolder,
            appCommandExecutorWithResult,
            sharedPreferences,
            screenResultObserver
        )
    }
}