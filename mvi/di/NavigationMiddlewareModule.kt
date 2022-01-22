package ru.arzonpay.android.ui.mvi.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.ui.freezer.LifecycleSubscriptionFreezer
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.arzonpay.android.ui.mvi.navigation.AppNavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware

/**
 * Модуль, который провайдит Middleware для навигации.
 */
@Module
class NavigationMiddlewareModule {

    @Provides
    @PerScreen
    fun provideNavigationMiddleware(
            navigationCommandExecutor: NavigationCommandExecutor,
            freezer: LifecycleSubscriptionFreezer
    ): NavigationMiddleware {
        return AppNavigationMiddleware(
                navigationCommandExecutor,
                freezer
        )
    }
}
