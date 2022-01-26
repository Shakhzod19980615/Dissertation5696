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
import ru.arzonpay.android.f_debug.tools.ToolsDebugActivityRoute
import ru.arzonpay.android.f_debug.tools.ToolsDebugActivityView

/**
 * Конфигуратор экрана показа общей информации
 */
class ToolsDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, ToolsDebugScreenModule::class]
    )
    interface ToolsDebugScreenComponent : ScreenComponent<ToolsDebugActivityView>

    @Module
    internal class ToolsDebugScreenModule(route: ToolsDebugActivityRoute)
        : DebugCustomScreenModule<ToolsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(parentComponent: DebugActivityComponent,
                                       activityScreenModule: DebugActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.DaggerToolsDebugScreenConfigurator_ToolsDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .toolsDebugScreenModule(ToolsDebugScreenModule(ToolsDebugActivityRoute()))
                .build()
    }
}
