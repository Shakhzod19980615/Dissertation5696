package ru.arzonpay.android.application.notification

import ru.arzonpay.android.application.notification.strategy.BasePushHandleStrategy
import ru.arzonpay.android.application.notification.strategy.TransactionCancelledPushHandleStrategy
import ru.arzonpay.android.application.notification.strategy.TransactionPushHandleStrategy
import ru.arzonpay.android.domain.notification.NotificationType
import ru.surfstudio.android.notification.ui.notification.AbstractPushHandleStrategyFactory
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import java.util.*

/**
 * Фабрика стратегий обработки пуша по его типу
 */
object PushHandleStrategyFactory : AbstractPushHandleStrategyFactory() {

    override val map: HashMap<String, PushHandleStrategy<*>> = hashMapOf(
        NotificationType.UNKNOWN.id to BasePushHandleStrategy(),
        NotificationType.TRANSACTION_SUCCESS.id to TransactionPushHandleStrategy(),
        NotificationType.TRANSACTION_CANCELLED.id to TransactionCancelledPushHandleStrategy()
    )
}