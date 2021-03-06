package ru.arzonpay.android.f_debug.fcm

import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.i_push_notification.storage.DeviceStorage
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class FcmDebugPresenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val resourceProvider: ResourceProvider,
    private val deviceStorage: DeviceStorage
) : BasePresenter<FcmDebugActivityView>(basePresenterDependency) {

    private val sm = FcmDebugScreenModel()

    override fun onFirstLoad() {
        super.onFirstLoad()
        loadFcmToken()
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun loadFcmToken() {
        sm.fcmToken = deviceStorage.fcmToken
        view.render(sm)
        logFcmToken()
    }

    fun copyFcmToken() {
        sm.fcmToken?.let {
            view.copyFcmToken()
            view.showMessage(resourceProvider.getString(R.string.debug_fcm_copied_message))
            logFcmToken()
        }
    }

    private fun logFcmToken() {
        sm.fcmToken?.apply {
            if (isNotEmpty()) {
                Logger.d("FCM-token: $this")
            }
        }
    }
}
