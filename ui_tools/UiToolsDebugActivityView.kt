package ru.arzonpay.android.f_debug.ui_tools

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.ActivityUiToolsDebugBinding
import ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.UiToolsDebugScreenConfigurator
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import javax.inject.Inject

/**
 * Вью экрана показа UI-tools
 */
class UiToolsDebugActivityView : BaseRenderableActivityView<UiToolsDebugScreenModel>() {

    private val vb by viewBinding(ActivityUiToolsDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: UiToolsDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = UiToolsDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_ui_tools_debug

    override fun getScreenName(): String = "UiToolsDebugActivityView"

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    override fun renderInternal(sm: UiToolsDebugScreenModel) {
        vb.debugFpsEnableSwitch.setChecked(sm.isFpsEnabled)
    }

    private fun initListeners() {
        vb.debugFpsEnableSwitch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setFpsEnable(isEnabled)
        }
        vb.debugScalpelTool.setOnClickListener {
            Toast.makeText(this, "Втряхните устройство для включения Scalpel", Toast.LENGTH_SHORT)
                .show()
        }
        vb.debugVqaTool.setOnClickListener { presenter.openWindowVQA() }
    }
}
