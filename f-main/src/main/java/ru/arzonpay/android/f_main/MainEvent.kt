package ru.arzonpay.android.f_main

import ru.arzonpay.android.domain.locale.Language
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsComposition
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent

internal sealed class MainEvent : Event {

    data class Navigation(override var event: NavCommandsEvent = NavCommandsEvent()) :
        NavCommandsComposition, MainEvent()

    data class Lifecycle(override var stage: LifecycleStage) : MainEvent(), LifecycleEvent

    data class LanguageChanged(val language: Language) : MainEvent()
}
