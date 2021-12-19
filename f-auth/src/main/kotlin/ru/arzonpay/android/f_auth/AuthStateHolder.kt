package ru.arzonpay.android.f_auth

import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

internal data class AuthState(
    val rawPhoneText: String = EMPTY_STRING,
    val extractedPhoneNumber: String = EMPTY_STRING,
    val phoneErrorText: String? = null,
    val loadState: LoadStateType = LoadStateType.None(),
    val isAuthBtnVisible: Boolean = true,
) {
    val isPhoneNumberValid = phoneErrorText == null

}

@PerScreen
internal class AuthStateHolder @Inject constructor() : State<AuthState>(
    AuthState()
)

@PerScreen
internal class AuthCommandHolder @Inject constructor() {
    val hideKeyboard: Command<Unit> = Command()
}