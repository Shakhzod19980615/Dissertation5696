package ru.arzonpay.android.f_confirmation

import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class WebConfirmationEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, WebConfirmationEvent()

    data class Lifecycle(override var stage: LifecycleStage) : WebConfirmationEvent(),
        LifecycleEvent

    sealed class Input : WebConfirmationEvent() {

        object LoadFinished : Input()
        object LoadError : Input()
        object LoadStarted : Input()
        object Reload : Input()
        object CloseClicked : Input()
        data class Redirect(val url: String) : Input()
    }
}