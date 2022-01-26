package ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.f_debug.injector.ui.DebugActivityComponent
import ru.arzonpay.android.f_debug.injector.ui.configurator.DebugActivityScreenConfigurator
import ru.arzonpay.android.f_debug.injector.ui.screen.DebugActivityScreenModule
import ru.arzonpay.android.f_debug.injector.ui.screen.DebugCustomScreenModule
import ru.arzonpay.android.f_debug.ui_tools.DebugOverlayPermissionChecker
import ru.arzonpay.android.f_debug.ui_tools.UiToolsDebugActivityRoute
import ru.arzonpay.android.f_debug.ui_tools.UiToolsDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class UiToolsDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, UiToolsDebugScreenModule::class]
    )
    interface UiToolsDebugScreenComponent : ScreenComponent<UiToolsDebugActivityView>

    @Module
    internal class UiToolsDebugScreenModule(route: UiToolsDebugActivityRoute)
        : DebugCustomScreenModule<UiToolsDebugActivityRoute>(route) {

        @Provides
        fun provideOverlayPermissionChecker(
                context: Context,
                activityNavigator: ActivityNavigator
        ): DebugOverlayPermissionChecker {
            return DebugOverlayPermissionChecker(context, activityNavigator)
        }
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: DebugActivityComponent,
                                       activityScreenModule: DebugActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.DaggerUiToolsDebugScreenConfigurator_UiToolsDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .uiToolsDebugScreenModule(UiToolsDebugScreenModule(UiToolsDebugActivityRoute()))
                .build()
    }
}
