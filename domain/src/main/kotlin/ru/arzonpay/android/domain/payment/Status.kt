package ru.arzonpay.android.domain.payment

enum class Status(val id: String) {
    CREATED("created"),
    PROCESSING("in progress"),
    DECLINED("canceled"),
    SUCCESS("success");

    companion object {
        fun fromId(id: String?): Status {
            return values().firstOrNull { it.id == id } ?: PROCESSING
        }
    }
}