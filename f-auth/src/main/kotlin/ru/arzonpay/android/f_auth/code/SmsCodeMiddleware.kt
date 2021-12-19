package ru.arzonpay.android.f_auth.code

import io.reactivex.Observable
import ru.arzonpay.android.f_auth.code.SmsCodeEvent.*
import ru.arzonpay.android.i_auth.AuthInteractor
import ru.arzonpay.android.i_auth.firebase.AuthResponse
import ru.arzonpay.android.i_auth.sms.SmsRetrieverService
import ru.arzonpay.android.ui.analytics.event.sms.CodeErrorEvent
import ru.arzonpay.android.ui.analytics.event.sms.CodeSuccessEvent
import ru.arzonpay.android.ui.analytics.event.sms.ResendCodeEvent
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.removeLast
import ru.arzonpay.android.ui.mvi.navigation.extension.replaceHard
import ru.arzonpay.android.ui.navigation.routes.MainBarRoute
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class SmsCodeMiddleware @Inject constructor(
    baseMiddlewareDependency: BaseMiddlewareDependency,
    private val authInteractor: AuthInteractor,
    private val analyticService: DefaultAnalyticService,
    private val navigationMiddleware: NavigationMiddleware,
    private val sh: SmsCodeStateHolder,
    private val smsRetrieverService: SmsRetrieverService,
) : BaseMiddleware<SmsCodeEvent>(baseMiddlewareDependency) {

    private val state: SmsCodeState get() = sh.value

    override fun transform(eventStream: Observable<SmsCodeEvent>) = transformations(eventStream) {
        addAll(
            Navigation::class decomposeTo navigationMiddleware,
            onCreate() map { TimerTick },
            Input.CloseClicked::class mapTo { Navigation().removeLast() },

            Input.ResendCode::class eventMapTo { onCodeResend() },
            Input.ResendCode::class reactTo { startSmsListener() },
            Input.CodeChanged::class filter { it.code.length == state.digitNumber } eventMap ::onCodeEntered,
            TimerTick::class streamMapTo ::onTimerTick,

            DataLoad.ResendCode::class filter { it.isSuccess && it.request.getData() is AuthResponse.CodeSent } map { TimerTick },
            DataLoad.ResendCode::class filter { it.isSuccess && it.request.getData() is AuthResponse.AuthSuccess } map { openMainScreen() },
            DataLoad.CheckCode::class filter { it.isSuccess } map { openMainScreen() },

            observeSmsCode(),

            Input.ResendCode::class reactTo { sendResendCodeEvent() },
            DataLoad.CheckCode::class filter { !it.isLoading } react { onCheckCode(it.request) }
        )
    }

    private fun onCodeResend(): Observable<out SmsCodeEvent> {
        return authInteractor.requestCode("+7" + state.phone)
            .io()
            .asRequestEvent(DataLoad::ResendCode)
    }

    private fun onTimerTick(observable: Observable<TimerTick>): Observable<out SmsCodeEvent> {
        return observable
            .filter { !state.isResendAllowed }
            .map { TimerTick }
            .delay(1, TimeUnit.SECONDS)
    }

    private fun onCodeEntered(event: Input.CodeChanged): Observable<out SmsCodeEvent> {
        return authInteractor.checkSms(state.key, state.inputtedCode)
            .io()
            .asRequestEvent { DataLoad.CheckCode(it) }
    }

    private fun openMainScreen(): SmsCodeEvent {
        return Navigation().replaceHard(
            MainBarRoute(),
            sourceTag = FragmentNavigationCommand.ACTIVITY_NAVIGATION_TAG
        )
    }

    private fun startSmsListener() {
        smsRetrieverService.startObserveMessage()
    }

    private fun observeSmsCode(): Observable<out SmsCodeEvent> {
        return smsRetrieverService.codeObservable
            .map { Input.CodeChanged(it) }
    }

    private fun sendResendCodeEvent() {
        analyticService.performAction(ResendCodeEvent())
    }

    private fun onCheckCode(request: Request<Unit>) {
        if (request.isSuccess) {
            analyticService.performAction(CodeSuccessEvent())
        } else {
            analyticService.performAction(CodeErrorEvent())
        }
    }
}