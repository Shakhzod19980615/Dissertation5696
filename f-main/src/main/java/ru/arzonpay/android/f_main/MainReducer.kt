package ru.arzonpay.android.f_main

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.*
import javax.inject.Inject

internal class MainState()

/**
 * State Holder [MainActivityView]
 */
@PerScreen
internal class MainScreenStateHolder @Inject constructor(
) : State<MainState>(MainState())

@PerScreen
internal class MainScreenCommandHolder @Inject constructor() {
    val localeChanged: Command<Locale> = Command()
}

/**
 * Reducer [MainActivityView]
 */
@PerScreen
internal class MainReducer @Inject constructor(
    dependency: BaseReactorDependency,
    private val ch: MainScreenCommandHolder
) : BaseReducer<MainEvent, MainState>(dependency) {

    override fun reduce(state: MainState, event: MainEvent): MainState {
        return when (event) {
            is MainEvent.LanguageChanged -> onLanguageChanged(state, event)
            else -> state
        }
    }

    private fun onLanguageChanged(state: MainState, event: MainEvent.LanguageChanged): MainState {
        ch.localeChanged.accept(Locale(event.language.code))
        return state
    }
}