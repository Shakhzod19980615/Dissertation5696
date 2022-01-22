package ru.arzonpay.android.domain.payment

import java.io.Serializable

const val CARD_TRANSFER_ID = "6"
const val CARD_TRANSFER_CARD_KEY = "card"
const val TRANSFER_AMOUNT_KEY = "amount"

/**
 * @property hasSubtypes есть ли подтипы
 */
data class Provider(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val hasSubtypes: Boolean = false
) : Serializable {
    val isCardTransfer: Boolean = id == CARD_TRANSFER_ID
}