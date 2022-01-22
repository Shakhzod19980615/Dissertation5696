package ru.arzonpay.android.application.notification.type

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.arzonpay.android.domain.notification.Notification
import ru.arzonpay.android.domain.notification.NotificationType
import ru.arzonpay.android.domain.payment.Transaction
import ru.arzonpay.android.i_payment.response.TransactionObj
import ru.arzonpay.android.i_payment.response.toObj
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData

/**
 * Тип пуш уведомления с данными
 */
class NotificationTypeData : BaseNotificationTypeData<Notification>() {

    override fun extractData(map: Map<String, String>) = Notification(
        type = NotificationType.getById(map["event"]),
        transaction = getTransaction(map)
    )

    fun dataToMap(): Map<String, String> {
        val notification = data ?: return mapOf()
        return mapOf(
            "event" to notification.type.id,
            "transaction" to parseTransaction(notification.transaction)
        )
    }

    private fun getTransaction(map: Map<String, String>): Transaction? {
        val typeToken = object : TypeToken<TransactionObj>() {}.type
        return Gson().fromJson<TransactionObj>(map["transaction"], typeToken)?.transform()
    }

    private fun parseTransaction(transaction: Transaction?): String {
        val obj = transaction?.toObj()
        return Gson().toJson(obj)
    }
}