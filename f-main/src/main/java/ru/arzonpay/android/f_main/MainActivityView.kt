package ru.arzonpay.android.f_main

import android.content.Context
import android.content.res.Configuration
import ru.arzonpay.android.application.app.di.AppInjector
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.arzonpay.android.f_main.di.MainScreenConfigurator
import ru.arzonpay.android.ui.mvi.view.BaseMviActivityView
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.arzonpay.android.f_main.databinding.ActivityMainBinding
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.arzonpay.android.ui.util.LocaleChanger
import ru.surfstudio.android.logger.Logger
import java.util.*
import javax.inject.Inject

/**
 * Вью главного экрана
 */
internal class MainActivityView : BaseMviActivityView<MainState, MainEvent>(),
    FragmentNavigationContainer {

    @Inject
    override lateinit var hub: ScreenEventHub<MainEvent>

    @Inject
    override lateinit var sh: MainScreenStateHolder

    @Inject
    lateinit var ch: MainScreenCommandHolder

    private val binding by viewBinding(ActivityMainBinding::bind) { rootView }

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override val containerId: Int
        get() = R.id.main_fragment_container

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun initViews() {
        ch.localeChanged bindTo { recreate() }
    }

    override fun render(state: MainState) {
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleChanger.updateConfiguration(
                newBase,
                Locale(AppInjector.appComponent.settingsInteractor().getLanguage().code)
            )
        )
    }
}
