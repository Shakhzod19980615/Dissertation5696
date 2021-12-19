package ru.arzonpay.android.f_auth.code.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_auth.code.*
import ru.arzonpay.android.f_auth.code.SmsCodeEvent
import ru.arzonpay.android.f_auth.code.SmsCodeFragmentRoute
import ru.arzonpay.android.f_auth.code.SmsCodeFragmentView
import ru.arzonpay.android.f_auth.code.SmsCodeMiddleware
import ru.arzonpay.android.f_auth.code.SmsCodeStateHolder
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.FragmentScreenConfigurator
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen

class SmsCodeScreenConfigurator(args: Bundle?) : FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, SmsCodeScreenModule::class]
    )
    internal interface SmsCodeScreenComponent : BindableScreenComponent<SmsCodeFragmentView>

    @Module
    internal class SmsCodeScreenModule(route: SmsCodeFragmentRoute) :
        CustomScreenModule<SmsCodeFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
            screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<SmsCodeEvent>(screenEventHubDependency, SmsCodeEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<SmsCodeEvent>,
            mw: SmsCodeMiddleware,
            sh: SmsCodeStateHolder,
            reducer: SmsCodeReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle
    ): ScreenComponent<*> {
        return DaggerSmsCodeScreenConfigurator_SmsCodeScreenComponent.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .smsCodeScreenModule(SmsCodeScreenModule(SmsCodeFragmentRoute(args)))
            .build()
    }
}
