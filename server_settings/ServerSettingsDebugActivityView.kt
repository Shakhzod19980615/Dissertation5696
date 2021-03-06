package ru.arzonpay.android.f_debug.server_settings

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.widget.RxSeekBar
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.ActivityServerSettingsDebugBinding
import ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.ServerSettingsDebugScreenConfigurator
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import javax.inject.Inject

/**
 * Вью экрана настроек сервера
 */
class ServerSettingsDebugActivityView :
    BaseRenderableActivityView<ServerSettingsDebugScreenModel>() {

    private val vb by viewBinding(ActivityServerSettingsDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: ServerSettingsDebugPresenter

    override fun getContentView() = R.layout.activity_server_settings_debug

    override fun getScreenName() = "ServerSettingsDebugActivityView"

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = ServerSettingsDebugScreenConfigurator(intent)

    override fun renderInternal(sm: ServerSettingsDebugScreenModel) = with(vb) {
        debugServerSettingsChuckSwitch.setChecked(sm.isChuckEnabled)
        debugServerSettingsTestServerSwitch.setChecked(sm.isTestServerEnabled)
        debugServerSettingsRequestDelayTv.text =
            getString(R.string.debug_server_settings_request_delay_text, sm.requestDelaySeconds)
        debugServerSettingsRequestDelaySeekBar.progress = sm.requestDelayCoefficient
    }

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    private fun initListeners() = with(vb) {
        debugServerSettingsChuckSwitch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setChuckEnabled(isEnabled)
        }
        debugServerSettingsTestServerSwitch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setTestServerEnabled(isEnabled)
        }
        presenter.requestDelayCoefficientChanges(
            RxSeekBar.userChanges(
                debugServerSettingsRequestDelaySeekBar
            ).skipInitialValue()
        )
    }
}