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
import ru.arzonpay.android.f_debug.reused_components.ReusedComponentsDebugActivityRoute
import ru.arzonpay.android.f_debug.reused_components.ReusedComponentsDebugActivityView

/**
 * Конфигуратор экрана для показа переиспользуемых компонентов
 */
class ReusedComponentsDebugScreenConfigurator(intent: Intent) : DebugActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DebugActivityComponent::class],
            modules = [DebugActivityScreenModule::class, ReusedComponentsDebugScreenModule::class]
    )
    interface ReusedComponentsDebugScreenComponent : ScreenComponent<ReusedComponentsDebugActivityView>

    @Module
    internal class ReusedComponentsDebugScreenModule(route: ReusedComponentsDebugActivityRoute)
        : DebugCustomScreenModule<ReusedComponentsDebugActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: DebugActivityComponent,
            activityScreenModule: DebugActivityScreenModule,
            intent: Intent
    ): ScreenComponent<*> {
        return ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.DaggerReusedComponentsDebugScreenConfigurator_ReusedComponentsDebugScreenComponent.builder()
                .debugActivityComponent(parentComponent)
                .debugActivityScreenModule(activityScreenModule)
                .reusedComponentsDebugScreenModule(ReusedComponentsDebugScreenModule(ReusedComponentsDebugActivityRoute()))
                .build()
    }
}
