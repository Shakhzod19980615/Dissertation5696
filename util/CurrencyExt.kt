package ru.arzonpay.android.ui.util

import androidx.annotation.DrawableRes
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.domain.Currency

@DrawableRes
fun Currency.getIcon(): Int {
    return when (this) {
        Currency.SUM -> R.drawable.ic_money_sum
        Currency.RUB -> R.drawable.ic_money_rub
    }
}