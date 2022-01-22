package ru.arzonpay.android.domain.login

/**
 * сущность, представляющая информацию о токене и его времени жизни
 */
data class LoginInfo(
    val accessToken: String,
)