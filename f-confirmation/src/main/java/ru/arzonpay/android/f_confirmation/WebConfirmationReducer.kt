package ru.arzonpay.android.f_confirmation

import ru.arzonpay.android.f_confirmation.WebConfirmationEvent.Input
import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class WebConfirmationReducer @Inject constructor(
    baseReducerDependency: BaseReactorDependency,
    private val ch: WebConfirmationCommandHolder
) : BaseReducer<WebConfirmationEvent, WebConfirmationState>(baseReducerDependency) {

    override fun reduce(
        state: WebConfirmationState,
        event: WebConfirmationEvent
    ): WebConfirmationState {
        return when (event) {
            is Input.LoadFinished -> onLoadFinished(state)
            is Input.LoadError -> state.copy(loadState = LoadStateType.None())
            is Input.LoadStarted -> state.copy(loadState = LoadStateType.Main)
            is Input.Reload -> onReload(state)
            else -> state
        }
    }

    private fun onLoadFinished(state: WebConfirmationState): WebConfirmationState {
        return if (state.loadState != LoadStateType.Error()) {
            state.copy(loadState = LoadStateType.None())
        } else {
            state
        }
    }

    private fun onReload(state: WebConfirmationState): WebConfirmationState {
        ch.reloadUrl.accept()
        return state
    }
}