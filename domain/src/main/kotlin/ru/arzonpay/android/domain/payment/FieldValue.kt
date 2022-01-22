package ru.arzonpay.android.domain.payment

import java.io.Serializable

data class FieldValue(
    val id: String,
    val value: String
) : Serializable