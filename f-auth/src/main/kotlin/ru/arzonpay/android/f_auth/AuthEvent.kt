package ru.arzonpay.android.f_auth

import ru.arzonpay.android.i_auth.firebase.AuthResponse
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class AuthEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, AuthEvent()

    data class Lifecycle(override var stage: LifecycleStage) : AuthEvent(), LifecycleEvent

    sealed class Input : AuthEvent() {
        object AnonymousAuthClicked : Input()
        data class PhoneEdited(val raw: String, val extracted: String) : Input()
        object SubmitClicked : Input()
    }

    data class VerificationCodeRequest(
        override val request: Request<AuthResponse>
    ) : RequestEvent<AuthResponse>, AuthEvent()


    data class AnonymousAuthRequest(
        override val request: Request<Unit>
    ) : RequestEvent<Unit>, AuthEvent()

    data class KeyboardHeightChanged(val height: Int, val screenHeight: Int) : AuthEvent()
}