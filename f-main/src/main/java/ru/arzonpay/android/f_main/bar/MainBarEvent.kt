package ru.arzonpay.android.f_main.bar

import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent
import ru.arzonpay.android.ui.mvi.save_state.SaveStateEvent
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.ui.state.LifecycleStage

internal sealed class MainBarEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, MainBarEvent()

    data class Lifecycle(override var stage: LifecycleStage) : MainBarEvent(), LifecycleEvent
    data class PersistentState(
        override var events: List<SaveStateEvent> = listOf()
    ) : CompositionEvent<SaveStateEvent>, MainBarEvent()

    object BackPressed : MainBarEvent()


    data class RegisterDeviceRequest(
        override val request: Request<Unit>
    ) : RequestEvent<Unit>, MainBarEvent()
}
