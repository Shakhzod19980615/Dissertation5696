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
import ru.arzonpay.android.f_debug.server_settings.ServerSettingsDebugActivityRoute
import ru.arzonpay.android.f_debug.server_settings.ServerSettingsDebugActivityView

/**
 * Конфигуратор экрана настроек сервера
 */
class ServerSettingsDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, ServerSettingsDebugScreenModule::class]
    )
    interface ServerSettingsDebugScreenComponent : ScreenComponent<ServerSettingsDebugActivityView>

    @Module
    internal class ServerSettingsDebugScreenModule(route: ServerSettingsDebugActivityRoute)
        : DebugCustomScreenModule<ServerSettingsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: DebugActivityComponent,
                                       activityScreenModule: DebugActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.DaggerServerSettingsDebugScreenConfigurator_ServerSettingsDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .serverSettingsDebugScreenModule(ServerSettingsDebugScreenModule(ServerSettingsDebugActivityRoute()))
                .build()
    }
}