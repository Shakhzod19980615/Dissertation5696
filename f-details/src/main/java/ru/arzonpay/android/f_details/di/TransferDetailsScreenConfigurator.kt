package ru.arzonpay.android.f_details.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.f_details.*
import ru.arzonpay.android.ui.activity.di.ActivityComponent
import ru.arzonpay.android.ui.activity.di.FragmentScreenConfigurator
import ru.arzonpay.android.ui.navigation.routes.TransferDetailsFragmentRoute
import ru.arzonpay.android.ui.screen_modules.CustomScreenModule
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen

internal class TransferDetailsScreenConfigurator(arguments: Bundle) :
    FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
        dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, TransferDetailsScreenModule::class]
    )
    internal interface TransferDetailsScreenComponent :
        BindableScreenComponent<TransferDetailsFragmentView>

    @Module
    internal class TransferDetailsScreenModule(route: TransferDetailsFragmentRoute) :
        CustomScreenModule<TransferDetailsFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
            screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<TransferDetailsEvent>(
            screenEventHubDependency,
            TransferDetailsEvent::Lifecycle
        )

        @Provides
        @PerScreen
        fun provideBinder(
            screenBinderDependency: ScreenBinderDependency,
            eventHub: ScreenEventHub<TransferDetailsEvent>,
            mw: TransferDetailsMiddleware,
            sh: TransferDetailsStateHolder,
            reducer: TransferDetailsReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle
    ): ScreenComponent<*> {
        return DaggerTransferDetailsScreenConfigurator_TransferDetailsScreenComponent.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .transferDetailsScreenModule(
                TransferDetailsScreenModule(
                    TransferDetailsFragmentRoute(
                        args
                    )
                )
            )
            .build()
    }
}