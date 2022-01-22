package ru.arzonpay.android.domain.payment

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

class Transaction(
    val id: String,
    val provider: Provider,
    val fields: List<FormItem>,
    val userId: String,
    val uuid: String,
    val status: Status,
    val rate: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) : Serializable