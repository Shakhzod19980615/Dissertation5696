package ru.arzonpay.android.f_main.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_main.*
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.ActivityScreenConfigurator
import ru.arzonpay.android.ui.navigation.routes.MainActivityRoute
import ru.arzonpay.android.ui.screen_modules.ActivityScreenModule
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * Конфигуратор главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [ActivityScreenModule::class, MainScreenModule::class]
    )
    interface MainScreenComponent : BindableScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
        CustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(screenEventHubDependency: ScreenEventHubDependency) =
            ScreenEventHub<MainEvent>(screenEventHubDependency, MainEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<MainEvent>,
            middleware: MainMiddleware,
            sh: MainScreenStateHolder,
            reducer: MainReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, middleware, sh, reducer)
        }
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
        parentComponent: ActivityComponent,
        activityScreenModule: ActivityScreenModule,
        intent: Intent
    ): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
            .activityComponent(parentComponent)
            .activityScreenModule(activityScreenModule)
            .mainScreenModule(MainScreenModule(MainActivityRoute(intent)))
            .build()
    }
}