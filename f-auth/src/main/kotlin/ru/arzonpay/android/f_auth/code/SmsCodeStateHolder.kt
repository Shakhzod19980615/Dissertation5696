package ru.arzonpay.android.f_auth.code

import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

internal const val CODE_RESEND_INTERVAL = 60

internal data class SmsCodeState(
    val phone: String,
    val key: String,
    val secondsToResend: Int = CODE_RESEND_INTERVAL,
    val digitNumber: Int = 6,
    val message: String = EMPTY_STRING,
    val inputtedCode: String = EMPTY_STRING,
    val timeText: String = EMPTY_STRING,
    val loadState: LoadStateType = LoadStateType.None(),
    val hasError: Boolean = false,
) {
    val isResendAllowed: Boolean
        get() = secondsToResend == 0
}

@PerScreen
internal class SmsCodeStateHolder @Inject constructor(
    route: SmsCodeFragmentRoute
) : State<SmsCodeState>(
    SmsCodeState(
        phone = route.phone,
        key = route.key
    )
)

@PerScreen
internal class SmsCodeCommandHolder @Inject constructor() {
    val makeErrorVibration = Command<Unit>()
    val hideKeyboard = Command<Unit>()
}