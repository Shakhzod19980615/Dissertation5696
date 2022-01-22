package ru.arzonpay.android.ui.analytics.event.feed

enum class CardTransferErrorType(val value: String) {
    CARD_NUMBER("card_number"), MONEY("money"), BOTH("both");

    companion object {
        fun fromErrors(isAmountError: Boolean, isCardError: Boolean): CardTransferErrorType {
            return when {
                isAmountError && isCardError -> BOTH
                isAmountError -> MONEY
                else -> CARD_NUMBER
            }
        }
    }
}