package ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.f_debug.injector.ui.DebugActivityComponent
import ru.arzonpay.android.f_debug.injector.ui.configurator.DebugActivityScreenConfigurator
import ru.arzonpay.android.f_debug.injector.ui.screen.DebugActivityScreenModule
import ru.arzonpay.android.f_debug.injector.ui.screen.DebugCustomScreenModule
import ru.arzonpay.android.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.arzonpay.android.f_debug.server_settings.reboot.RebootDebugActivityView

/**
 * Конфигуратор экрана перезапуска приложения
 */
class RebootDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, RebootDebugScreenModule::class]
    )
    interface RebootDebugScreenComponent : ScreenComponent<RebootDebugActivityView>

    @Module
    internal class RebootDebugScreenModule(route: RebootDebugActivityRoute)
        : DebugCustomScreenModule<RebootDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: DebugActivityComponent,
                                       activityScreenModule: DebugActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.DaggerRebootDebugScreenConfigurator_RebootDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .rebootDebugScreenModule(RebootDebugScreenModule(RebootDebugActivityRoute()))
                .build()
    }
}