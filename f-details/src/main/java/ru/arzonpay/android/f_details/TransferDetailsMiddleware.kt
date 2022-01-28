package ru.arzonpay.android.f_details

import io.reactivex.Observable
import ru.arzonpay.android.f_details.TransferDetailsEvent.*
import ru.arzonpay.android.i_payment.PaymentInteractor
import ru.arzonpay.android.ui.animation.ModalAnimations
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.removeLast
import ru.arzonpay.android.ui.mvi.navigation.extension.replace
import ru.arzonpay.android.ui.navigation.routes.FormFragmentRoute
import ru.arzonpay.android.ui.navigation.routes.SupportFragmentRoute
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class TransferDetailsMiddleware @Inject constructor(
    dependency: BaseMiddlewareDependency,
    private val navigationMiddleware: NavigationMiddleware,
    private val paymentInteractor: PaymentInteractor,
    private val sh: TransferDetailsStateHolder
) : BaseMiddleware<TransferDetailsEvent>(dependency) {

    private val state: TransferDetailsState
        get() = sh.value

    override fun transform(eventStream: Observable<TransferDetailsEvent>): Observable<out TransferDetailsEvent> =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                onCreate() eventMap { loadForm() },
                Input.BackClicked::class mapTo { Navigation().removeLast() },
                Input.SupportClicked::class mapTo { openSupport() },
                Input.ReplyClicked::class mapTo { openFormScreen() }
            )
        }

    private fun loadForm(): Observable<out TransferDetailsEvent> {
        return paymentInteractor.getForm(state.transaction.provider.id)
            .io()
            .asRequestEvent(::FormRequestEvent)
    }

    private fun openFormScreen(): TransferDetailsEvent {
        return Navigation().replace(
            FormFragmentRoute(state.transaction.provider, state.transaction.fields)
        )
    }

    private fun openSupport(): TransferDetailsEvent {
        return Navigation().replace(SupportFragmentRoute(), animations = ModalAnimations)
    }
}