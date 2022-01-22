package ru.arzonpay.android.f_confirmation

import ru.arzonpay.android.i_network.network.BaseUrl
import ru.arzonpay.android.ui.navigation.routes.WebConfirmationFragmentRoute
import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class WebConfirmationState(
    val url: String,
    val title: String,
    val loadState: LoadStateType = LoadStateType.Main
)

@PerScreen
internal class WebConfirmationStateHolder @Inject constructor(
    route: WebConfirmationFragmentRoute,
    baseUrl: BaseUrl
) : State<WebConfirmationState>(
    WebConfirmationState(url = "$baseUrl/pay/${route.uuid}", title = route.title)
)

@PerScreen
internal class WebConfirmationCommandHolder @Inject constructor() {

    val reloadUrl = Command<Unit>()
}