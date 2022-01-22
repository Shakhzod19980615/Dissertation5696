package ru.arzonpay.android.domain.notification

import ru.arzonpay.android.domain.payment.Transaction
import java.io.Serializable

class Notification(
    val type: NotificationType,
    val transaction: Transaction? = null
) : Serializable