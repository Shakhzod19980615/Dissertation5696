package ru.arzonpay.android.f_auth

import io.reactivex.Observable
import ru.arzonpay.android.f_auth.AuthEvent.*
import ru.arzonpay.android.f_auth.code.SmsCodeFragmentRoute
import ru.arzonpay.android.i_auth.AuthInteractor
import ru.arzonpay.android.i_auth.firebase.AuthResponse
import ru.arzonpay.android.i_auth.sms.SmsRetrieverService
import ru.arzonpay.android.ui.analytics.event.auth.*
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.replace
import ru.arzonpay.android.ui.mvi.navigation.extension.replaceHard
import ru.arzonpay.android.ui.navigation.routes.MainBarRoute
import ru.arzonpay.android.ui.util.mvi.CLICK_DELAY
import ru.arzonpay.android.ui.util.mvi.withThrottle
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import javax.inject.Inject

@PerScreen
internal class AuthMiddleware @Inject constructor(
    baseMiddlewareDependency: BaseMiddlewareDependency,
    private val authInteractor: AuthInteractor,
    private val analyticService: DefaultAnalyticService,
    private val navigationMiddleware: NavigationMiddleware,
    private val sh: AuthStateHolder,
    private val smsRetrieverService: SmsRetrieverService,
) : BaseMiddleware<AuthEvent>(baseMiddlewareDependency) {

    private val state: AuthState get() = sh.value

    override fun transform(eventStream: Observable<AuthEvent>): Observable<out AuthEvent> =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                Input.SubmitClicked::class filter { state.isPhoneNumberValid } eventMap { getCode() }
                        withThrottle CLICK_DELAY,
                Input.SubmitClicked::class filter { state.isPhoneNumberValid } react { startSmsListener() }
                        withThrottle CLICK_DELAY,
                Input.AnonymousAuthClicked::class eventMapTo { authAnonymously() } withThrottle CLICK_DELAY,
                VerificationCodeRequest::class.filter { it.isSuccess }.map { it.request.getData() }
                    .map { handleAuthResponse(it) },
                AnonymousAuthRequest::class filter { it.isSuccess } map { openMainScreen() },
                VerificationCodeRequest::class filter { !it.isLoading } react {
                    handleVerificationEvent(it.request)
                },
                AnonymousAuthRequest::class filter { !it.isLoading } react {
                    handleAnonymousAuthRequest(it.request)
                },
                Input.SubmitClicked::class reactTo { sendPhoneValidationEvent() } withThrottle CLICK_DELAY,
            )
        }

    private fun getCode(): Observable<out AuthEvent> {
        return authInteractor.requestCode(PHONE_PREFIX + state.extractedPhoneNumber)
            .io()
            .asRequestEvent(::VerificationCodeRequest)
    }

    private fun authAnonymously(): Observable<out AuthEvent> {
        return authInteractor.signInAnonymous()
            .io()
            .asRequestEvent(::AnonymousAuthRequest)
    }

    private fun startSmsListener() {
        smsRetrieverService.startObserveMessage()
    }

    private fun handleAuthResponse(result: AuthResponse): AuthEvent {
        return when (result) {
            is AuthResponse.CodeSent -> openCodeScreen(result)
            is AuthResponse.AuthSuccess -> openMainScreen()
            else -> Navigation()
        }
    }

    private fun openMainScreen(): AuthEvent {
        return Navigation().replaceHard(
            MainBarRoute(),
            sourceTag = FragmentNavigationCommand.ACTIVITY_NAVIGATION_TAG
        )
    }

    private fun openCodeScreen(result: AuthResponse.CodeSent): AuthEvent {
        return Navigation().replace(SmsCodeFragmentRoute(state.extractedPhoneNumber, result.key))
    }

    private fun sendPhoneValidationEvent() {
        if (state.isPhoneNumberValid) {
            analyticService.performAction(PhoneValidEvent())
        } else {
            analyticService.performAction(PhoneInvalidEvent())
        }
    }

    private fun handleVerificationEvent(request: Request<AuthResponse>) {
        val event = when (request.getDataOrNull()) {
            is AuthResponse.AuthSuccess -> AuthorizedEvent()
            is AuthResponse.CodeSent -> ReceiveCodeSuccessEvent()
            else -> ErrorEvent()
        }
        analyticService.performAction(event)
    }

    private fun handleAnonymousAuthRequest(request: Request<Unit>) {
        val event = when (request.isSuccess) {
            true -> AnonymousAuthSuccessEvent()
            else -> AnonymousAuthErrorEvent()
        }
        analyticService.performAction(event)
    }

    private companion object {
        const val PHONE_PREFIX = "+7"
    }
}