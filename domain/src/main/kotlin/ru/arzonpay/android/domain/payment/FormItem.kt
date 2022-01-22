package ru.arzonpay.android.domain.payment

import java.io.Serializable

data class FormItem(
    val name: String,
    val value: String
) : Serializable