package ru.arzonpay.android.domain.payment

/**
 * Информация о поле которое нужно заполнить для платежа
 *
 * @property name
 */
data class Field(
    val id: String,
    val name: String,
    val caption: String,
    val description: String,
    val order: Int,
    val type: FieldType,
    val regex: Regex? = null,
    val relation: String? = null,
    val errorText: String
)