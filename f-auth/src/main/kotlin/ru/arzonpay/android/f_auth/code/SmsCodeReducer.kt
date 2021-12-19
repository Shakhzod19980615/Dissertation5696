package ru.arzonpay.android.f_auth.code

import ru.arzonpay.android.f_auth.R
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.arzonpay.android.f_auth.code.SmsCodeEvent.*
import ru.arzonpay.android.ui.mvi.mapper.RequestMappers
import ru.arzonpay.android.ui.util.formatPhone
import ru.arzonpay.android.ui.util.loadStateType
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

@PerScreen
internal class SmsCodeReducer @Inject constructor(
    baseReactorDependency: BaseReactorDependency,
    private val resourceProvider: ResourceProvider,
    private val ch: SmsCodeCommandHolder
) : BaseReducer<SmsCodeEvent, SmsCodeState>(
    baseReactorDependency
) {

    override fun reduce(state: SmsCodeState, event: SmsCodeEvent): SmsCodeState {
        return when (event) {
            is Lifecycle -> initializeOnCreate(state, event)
            is TimerTick -> onTimerTick(state)
            is DataLoad.CheckCode -> onCheckCode(state, event)
            is DataLoad.ResendCode -> onResendCode(state, event)
            is Input.CodeChanged -> onCodeChanged(state, event)
            else -> state
        }
    }

    private fun initializeOnCreate(state: SmsCodeState, event: Lifecycle): SmsCodeState {
        if (event.stage != LifecycleStage.CREATED) return state

        return state.copy(
            message = resourceProvider.getString(
                R.string.sms_code_sent_phone,
                state.phone.notSkippablePhone()
            ),
            timeText = getResendInText(state.secondsToResend)
        )
    }

    private fun onTimerTick(state: SmsCodeState): SmsCodeState {
        val newSecondsToResend = (state.secondsToResend - 1).coerceAtLeast(0)
        return state.copy(
            secondsToResend = newSecondsToResend,
            timeText = getResendInText(newSecondsToResend)
        )
    }

    private fun onCodeChanged(state: SmsCodeState, event: Input.CodeChanged): SmsCodeState {
        val hasError = when {
            state.hasError && state.inputtedCode.isEmpty() && event.code.isNotEmpty() -> false
            else -> state.hasError
        }

        return state.copy(inputtedCode = event.code, hasError = hasError)
    }

    private fun onCheckCode(state: SmsCodeState, event: DataLoad.CheckCode): SmsCodeState {
        val request = event.request
        var newOtpCode = state.inputtedCode

        val newRequest = RequestMapper.builder(request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError { error, _, _ -> handleError(error) }
            .reactOnLoading { _ -> ch.hideKeyboard.accept() }
            .reactOnError { error ->
                newOtpCode = EMPTY_STRING
                ch.makeErrorVibration.accept()
            }
            .build()

        return state.copy(
            inputtedCode = newOtpCode,
            loadState = newRequest.loadStateType,
            hasError = request.isError
        )
    }

    private fun onResendCode(state: SmsCodeState, event: DataLoad.ResendCode): SmsCodeState {
        var newOtpCode = state.inputtedCode
        var newSecondsToResend = state.secondsToResend

        val newRequest = RequestMapper.builder(event.request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError { error, _, _ -> handleError(error) }
            .reactOnLoading { _ -> ch.hideKeyboard.accept() }
            .reactOnSuccess { nextOtp ->
                newOtpCode = EMPTY_STRING
                newSecondsToResend = CODE_RESEND_INTERVAL
            }
            .build()

        return state.copy(
            inputtedCode = newOtpCode,
            secondsToResend = newSecondsToResend,
            timeText = getResendInText(newSecondsToResend),
            loadState = newRequest.loadStateType
        )
    }

    private fun handleError(error: Throwable?): Boolean {
        error ?: return true
        errorHandler.handleError(error)
        return true
    }

    private fun getResendInText(secondsToResend: Int): String {
        val minutes = secondsToResend / 60
        val seconds = secondsToResend % 60
        return resourceProvider.getString(
            R.string.sms_code_resend_time,
            formatTime(minutes, seconds)
        )
    }

    private fun formatTime(minutes: Int, seconds: Int): String {
        val minutesText = when {
            minutes < 10 -> "0$minutes"
            else -> minutes.toString()
        }
        val secondsText = when {
            seconds < 10 -> "0$seconds"
            else -> seconds.toString()
        }
        return "$minutesText:$secondsText"
    }

    private fun String.notSkippablePhone(): String {
        return formatPhone().replace(SPACE, NO_BREAK_SPACE)
    }

    private companion object {
        const val SPACE = " "
        const val NO_BREAK_SPACE = "\u00A0"
    }
}