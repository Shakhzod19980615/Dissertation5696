package ru.arzonpay.android.domain.payment

enum class FieldType(val id: String) {

    CARD("card"),
    DIGITS("digits"),
    MONEY("money"),
    MULTILINE("multiline"),
    PHONE("phone"),
    SELECTOR("selector"),
    TEXT("text");

    companion object {
        fun getById(id: String?): FieldType {
            return values().firstOrNull { it.id == id } ?: TEXT
        }
    }
}