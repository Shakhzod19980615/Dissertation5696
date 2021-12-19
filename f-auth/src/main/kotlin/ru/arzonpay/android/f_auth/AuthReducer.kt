package ru.arzonpay.android.f_auth

import ru.arzonpay.android.f_auth.AuthEvent.*
import ru.arzonpay.android.ui.mvi.mapper.RequestMappers
import ru.arzonpay.android.ui.mvi.reducer.PhoneValidationReducer
import ru.arzonpay.android.ui.util.loadStateType
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class AuthReducer @Inject constructor(
    dependency: BaseReactorDependency,
    private val ch: AuthCommandHolder,
    private val resourceProvider: ResourceProvider
) : BaseReducer<AuthEvent, AuthState>(dependency), PhoneValidationReducer {

    override fun reduce(state: AuthState, event: AuthEvent): AuthState {
        return when (event) {
            is Input.PhoneEdited -> onPhoneEdited(state, event)
            is Input.SubmitClicked -> validatePhoneNumber(state)
            is VerificationCodeRequest -> onVerificationRequest(state, event)
            is AnonymousAuthRequest -> onAnonymousAuthRequest(state, event)
            is KeyboardHeightChanged -> onKeyboardHeightChanged(state, event)
            else -> state
        }
    }

    private fun onPhoneEdited(state: AuthState, event: Input.PhoneEdited): AuthState {
        return state.copy(
            rawPhoneText = event.raw,
            extractedPhoneNumber = event.extracted,
            phoneErrorText = null
        )
    }

    private fun validatePhoneNumber(state: AuthState): AuthState {
        val phoneNumberError = when {
            !validatePhone(state.extractedPhoneNumber) ->
                resourceProvider.getString(R.string.auth_phone_error)
            else -> {
                ch.hideKeyboard.accept()
                null
            }
        }
        return state.copy(phoneErrorText = phoneNumberError)
    }

    private fun onVerificationRequest(state: AuthState, event: VerificationCodeRequest): AuthState {
        val newRequestUi = RequestMapper.builder(event.request)
            .mapData(RequestMappers.data.single())
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError(RequestMappers.error.forced(errorHandler))
            .build()

        return state.copy(loadState = newRequestUi.loadStateType)
    }

    private fun onAnonymousAuthRequest(state: AuthState, event: AnonymousAuthRequest): AuthState {
        val newRequestUi = RequestMapper.builder(event.request)
            .mapData(RequestMappers.data.single())
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError(RequestMappers.error.forced(errorHandler))
            .build()

        return state.copy(loadState = newRequestUi.loadStateType)
    }

    private fun onKeyboardHeightChanged(state: AuthState, event: KeyboardHeightChanged): AuthState {
        return if (event.screenHeight - event.height > 354.toPx) {
            state.copy(isAuthBtnVisible = true)
        } else {
            state.copy(isAuthBtnVisible = false)
        }
    }
}