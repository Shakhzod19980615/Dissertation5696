package ru.arzonpay.android.domain.notification

/**
 * Типы уведомлений
 */
enum class NotificationType(val id: String) {

    UNKNOWN(""),
    TRANSACTION_SUCCESS("transaction_success"),
    TRANSACTION_CANCELLED("transaction_cancelled");

    companion object {
        fun getById(id: String?) = values().firstOrNull { it.id == id } ?: UNKNOWN
    }
}