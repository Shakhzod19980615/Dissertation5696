package ru.arzonpay.android.f_debug.server_settings.reboot

import android.os.Bundle
import android.os.PersistableBundle
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.ActivityRebootDebugBinding
import ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.RebootDebugScreenConfigurator
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import javax.inject.Inject

/**
 * Вью экрана перезапуска приложения
 */
class RebootDebugActivityView : BaseRenderableActivityView<RebootDebugScreenModel>() {

    private val vb by viewBinding(ActivityRebootDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: RebootDebugPresenter

    override fun getContentView() = R.layout.activity_reboot_debug

    override fun getScreenName() = "RebootDebugActivityView"

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = RebootDebugScreenConfigurator(intent)

    override fun renderInternal(sm: RebootDebugScreenModel) {
        vb.debugRebootTw.text =
            getString(R.string.debug_reboot_tw_start_text, sm.secondBeforeReboot.toString())
    }

    override fun onBackPressed() {
        // отключаем возможность закрыть активность
    }

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    private fun initListeners() {
        vb.debugRebootNowB.setOnClickListener { presenter.rebootNow() }
        vb.debugRebootCancelB.setOnClickListener { presenter.cancelReboot() }
    }
}