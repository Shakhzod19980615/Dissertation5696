package ru.arzonpay.android.f_debug.debug

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.f_debug.reused_components.ReusedComponentsDebugActivityRoute
import ru.arzonpay.android.f_debug.system_settings.DeveloperToolsDebugActivityRoute
import ru.arzonpay.android.f_debug.fcm.FcmDebugActivityRoute
import ru.arzonpay.android.f_debug.info.AppInfoDebugActivityRoute
import ru.arzonpay.android.f_debug.memory.MemoryDebugActivityRoute
import ru.arzonpay.android.f_debug.server_settings.ServerSettingsDebugActivityRoute
import ru.arzonpay.android.f_debug.system_settings.AppSettingsDebugActivityRoute
import ru.arzonpay.android.f_debug.ui_tools.UiToolsDebugActivityRoute
import ru.arzonpay.android.f_debug.tools.ToolsDebugActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа информации для дебага
 */
@PerScreen
class DebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<DebugActivityView>(basePresenterDependency) {

    fun openServerSettingsScreen() {
        activityNavigator.start(ServerSettingsDebugActivityRoute())
    }

    fun openReusedComponentsScreen() {
        activityNavigator.start(ReusedComponentsDebugActivityRoute())
    }

    fun openFcmTokenScreen() {
        activityNavigator.start(FcmDebugActivityRoute())
    }

    fun openMemoryScreen() {
        activityNavigator.start(MemoryDebugActivityRoute())
    }

    fun openAppInfoScreen() {
        activityNavigator.start(AppInfoDebugActivityRoute())
    }

    fun openUiToolsScreen() {
        activityNavigator.start(UiToolsDebugActivityRoute())
    }

    fun openDeveloperToolsScreen() {
        activityNavigator.start(DeveloperToolsDebugActivityRoute())
    }

    fun openToolsScreen() {
        activityNavigator.start(ToolsDebugActivityRoute())
    }

    fun openAppSettingsScreen() {
        activityNavigator.start(AppSettingsDebugActivityRoute())
    }
}
