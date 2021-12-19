package ru.arzonpay.android.f_auth.code

import ru.arzonpay.android.i_auth.firebase.AuthResponse
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class SmsCodeEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, SmsCodeEvent()

    data class Lifecycle(override var stage: LifecycleStage) : SmsCodeEvent(), LifecycleEvent

    object TimerTick : SmsCodeEvent()

    sealed class Input : SmsCodeEvent() {
        object CloseClicked : Input()
        object ResendCode : Input()
        data class CodeChanged(val code: String) : Input()
    }

    sealed class DataLoad : SmsCodeEvent() {

        data class ResendCode(
            override val request: Request<AuthResponse>
        ) : RequestEvent<AuthResponse>, DataLoad()

        data class CheckCode(
            override val request: Request<Unit>
        ) : RequestEvent<Unit>, DataLoad()
    }
}