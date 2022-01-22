package ru.arzonpay.android.f_confirmation.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_confirmation.*
import ru.arzonpay.android.f_confirmation.WebConfirmationEvent
import ru.arzonpay.android.f_confirmation.WebConfirmationFragmentView
import ru.arzonpay.android.f_confirmation.WebConfirmationMiddleware
import ru.arzonpay.android.f_confirmation.WebConfirmationReducer
import ru.arzonpay.android.f_confirmation.WebConfirmationStateHolder
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.FragmentScreenConfigurator
import ru.arzonpay.android.ui.navigation.routes.WebConfirmationFragmentRoute
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор таба новости [WebConfirmationFragmentView]
 */
internal class WebConfirmationScreenConfigurator(arguments: Bundle) :
    FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, WebScreenModule::class]
    )
    internal interface WebConfirmationScreenComponent :
        BindableScreenComponent<WebConfirmationFragmentView>

    @Module
    internal class WebScreenModule(route: WebConfirmationFragmentRoute) :
        CustomScreenModule<WebConfirmationFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
            screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<WebConfirmationEvent>(
            screenEventHubDependency,
            WebConfirmationEvent::Lifecycle
        )

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<WebConfirmationEvent>,
            mw: WebConfirmationMiddleware,
            sh: WebConfirmationStateHolder,
            reducer: WebConfirmationReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle
    ): ScreenComponent<*> {
        return DaggerWebConfirmationScreenConfigurator_WebConfirmationScreenComponent.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .webScreenModule(WebScreenModule(WebConfirmationFragmentRoute(args)))
            .build()
    }
}
