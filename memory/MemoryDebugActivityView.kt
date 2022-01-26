package ru.arzonpay.android.f_debug.memory

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.ActivityMemoryDebugBinding
import ru.arzonpay.android.f_debug.injector.ui.screen.configurator.activity.MemoryDebugScreenConfigurator
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import javax.inject.Inject

class MemoryDebugActivityView : BaseRenderableActivityView<MemoryDebugScreenModel>() {

    private val vb by viewBinding(ActivityMemoryDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: MemoryDebugPresenter

    override fun createConfigurator() = MemoryDebugScreenConfigurator(intent)

    override fun getScreenName() = "MemoryDebugActivityView"

    override fun getContentView() = R.layout.activity_memory_debug

    override fun getPresenters() = arrayOf(presenter)

    override fun renderInternal(model: MemoryDebugScreenModel) {
        vb.debugMemoryLeakcanarySwitch.setChecked(model.isLeakCanaryEnabled)
        initListeners()
    }

    private fun initListeners() {
        vb.debugMemoryLeakcanarySwitch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setLeakCanaryEnabled(isEnabled)
        }
        vb.debugShowStorageItemLayout.setOnClickListener { presenter.openStorageDebugScreen() }
    }
}