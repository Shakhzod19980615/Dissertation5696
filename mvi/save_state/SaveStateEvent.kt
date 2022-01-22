package ru.arzonpay.android.ui.mvi.save_state

import android.os.Bundle
import ru.surfstudio.android.core.mvi.event.Event

/**
 * Базовые события сохранения состояния при смене конфигурации
 */
sealed class SaveStateEvent : Event {
    data class Restore(val state: Bundle?) : SaveStateEvent()
    data class Save(val state: Bundle?) : SaveStateEvent()
}
