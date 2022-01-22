package ru.arzonpay.android.f_confirmation

import io.reactivex.Observable
import ru.arzonpay.android.f_confirmation.WebConfirmationEvent.Input
import ru.arzonpay.android.f_confirmation.WebConfirmationEvent.Navigation
import ru.arzonpay.android.ui.analytics.event.confirm.ConfirmationErrorEvent
import ru.arzonpay.android.ui.analytics.event.confirm.ConfirmationSuccessEvent
import ru.arzonpay.android.ui.animation.SlideAnimations
import ru.arzonpay.android.ui.dialog.base.simple.SimpleResult
import ru.arzonpay.android.ui.dialog.simple.SimpleDialogRoute
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.removeUntil
import ru.arzonpay.android.ui.mvi.navigation.extension.show
import ru.arzonpay.android.ui.navigation.routes.FeedFragmentRoute
import ru.arzonpay.android.ui.navigation.routes.WebConfirmationFragmentRoute
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import ru.surfstudio.android.rx.extension.toObservable
import javax.inject.Inject

@PerScreen
internal class WebConfirmationMiddleware @Inject constructor(
    baseMiddlewareDependency: BaseMiddlewareDependency,
    private val analyticService: DefaultAnalyticService,
    private val navigationMiddleware: NavigationMiddleware,
    private val screenResultEmitter: ScreenResultEmitter,
    private val screenResultObserver: ScreenResultObserver,
    private val route: WebConfirmationFragmentRoute
) : BaseMiddleware<WebConfirmationEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<WebConfirmationEvent>) =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                Input.Redirect::class eventMapTo { handleRedirect(it.url) },
                Input.CloseClicked::class mapTo { openCloseConfirmation() },
                Input.LoadError::class reactTo { sendConfirmationErrorEvent() },
                observeConfirmDialogResult()
            )
        }

    private fun openCloseConfirmation(): WebConfirmationEvent {
        return Navigation().show(
            SimpleDialogRoute(
                dialogId = CONFIRM_DIALOG_ID,
                messageRes = R.string.confirmation_exit_message,
            )
        )
    }

    private fun observeConfirmDialogResult(): Observable<out WebConfirmationEvent> {
        return screenResultObserver
            .observeScreenResult(SimpleDialogRoute(CONFIRM_DIALOG_ID))
            .filter { it == SimpleResult.POSITIVE }
            .map { closePaymentFlow() }
    }

    private fun handleRedirect(url: String): Observable<out WebConfirmationEvent> {
        return when {
            url.contains(SUCCESS_URL) -> {
                sendConfirmationSuccessEvent()
                closePaymentFlow(SimpleResult.POSITIVE).toObservable()
            }
            else -> skip()
        }
    }

    private fun closePaymentFlow(result: SimpleResult = SimpleResult.NEGATIVE): WebConfirmationEvent {
        screenResultEmitter.emit(route, result)
        return Navigation().removeUntil(
            FeedFragmentRoute(),
            animations = SlideAnimations,
            isInclusive = false
        )
    }

    private fun sendConfirmationErrorEvent() {
        analyticService.performAction(ConfirmationErrorEvent())
    }

    private fun sendConfirmationSuccessEvent() {
        analyticService.performAction(ConfirmationSuccessEvent())
    }

    private companion object {
        const val SUCCESS_URL = "https://arzonpay.ru/success"
        const val CONFIRM_DIALOG_ID = "confirmation_dialog"
    }
}