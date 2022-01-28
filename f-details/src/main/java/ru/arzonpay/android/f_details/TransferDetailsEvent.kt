package ru.arzonpay.android.f_details

import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class TransferDetailsEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, TransferDetailsEvent()

    data class Lifecycle(override var stage: LifecycleStage) : TransferDetailsEvent(),
        LifecycleEvent

    sealed class Input : TransferDetailsEvent() {
        object BackClicked : Input()
        object ReplyClicked : Input()
        object SupportClicked : Input()
    }

    data class FormRequestEvent(
        override val request: Request<List<Field>>
    ) : RequestEvent<List<Field>>, TransferDetailsEvent()
}