package ru.arzonpay.android.f_auth.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_auth.*
import ru.arzonpay.android.f_auth.AuthEvent
import ru.arzonpay.android.f_auth.AuthFragmentView
import ru.arzonpay.android.f_auth.AuthMiddleware
import ru.arzonpay.android.f_auth.AuthStateHolder
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.FragmentScreenConfigurator
import ru.arzonpay.android.ui.navigation.routes.AuthFragmentRoute
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор таба новости [AuthFragmentView]
 */
internal class AuthScreenConfigurator(arguments: Bundle?) : FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, AuthScreenModule::class]
    )
    internal interface AuthScreenComponent : BindableScreenComponent<AuthFragmentView>

    @Module
    internal class AuthScreenModule(route: AuthFragmentRoute) :
        CustomScreenModule<AuthFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
            screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<AuthEvent>(screenEventHubDependency, AuthEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<AuthEvent>,
            mw: AuthMiddleware,
            sh: AuthStateHolder,
            reducer: AuthReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle?
    ): ScreenComponent<*> {
        return DaggerAuthScreenConfigurator_AuthScreenComponent.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .authScreenModule(AuthScreenModule(AuthFragmentRoute()))
            .build()
    }
}
