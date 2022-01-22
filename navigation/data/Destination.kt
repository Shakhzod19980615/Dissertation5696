package ru.arzonpay.android.ui.navigation.data

import ru.arzonpay.android.domain.payment.Transaction
import java.io.Serializable

sealed class Destination : Serializable {

    object None : Destination()
    data class TransactionScreen(val transaction: Transaction) : Destination()
}