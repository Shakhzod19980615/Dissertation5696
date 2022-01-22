package ru.arzonpay.android.ui.mvi.logger

import ru.surfstudio.android.core.mvi.impls.event.hub.logging.EventLogger
import ru.arzonpay.android.base_feature.BuildConfig.DEBUG

/**
 * Логгер, который записывает эвенты только в дебажной сборке.
 */
class DebugEventLogger : EventLogger() {

    override val shouldLog: Boolean = DEBUG
}
