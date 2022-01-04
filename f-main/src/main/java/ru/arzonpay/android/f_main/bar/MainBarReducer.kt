package ru.arzonpay.android.f_main.bar

import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class MainBarState(
    val stub: String = ""
)

@PerScreen
internal class MainBarStateHolder @Inject constructor() : State<MainBarState>(MainBarState())

@PerScreen
internal class MainBarReducer @Inject constructor(
    dependency: BaseReactorDependency
) : BaseReducer<MainBarEvent, MainBarState>(dependency) {

    override fun reduce(state: MainBarState, event: MainBarEvent): MainBarState {
        return when (event) {
            else -> state
        }
    }
}
