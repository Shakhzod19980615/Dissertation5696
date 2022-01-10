package ru.arzonpay.android.f_form.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_form.*
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.FragmentScreenConfigurator
import ru.arzonpay.android.ui.navigation.routes.FormFragmentRoute
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор таба новости [FormFragmentView]
 */
internal class FormScreenConfigurator(arguments: Bundle) : FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, FormScreenModule::class]
    )
    internal interface FormScreenComponent : BindableScreenComponent<FormFragmentView>

    @Module
    internal class FormScreenModule(route: FormFragmentRoute) :
        CustomScreenModule<FormFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
            screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<FormEvent>(screenEventHubDependency, FormEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<FormEvent>,
            mw: FormMiddleware,
            sh: FormStateHolder,
            reducer: FormReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle
    ): ScreenComponent<*> {
        return DaggerFormScreenConfigurator_FormScreenComponent.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .formScreenModule(FormScreenModule(FormFragmentRoute(args)))
            .build()
    }
}
