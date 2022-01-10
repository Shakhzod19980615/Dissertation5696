package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.ui.view.input.AmountInputLayout
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import java.math.BigDecimal

internal class MoneyInputItemController(
    private val onMoneyChanged: (FieldUi.Money, BigDecimal?, Currency) -> Unit
) : BindableItemController<FieldUi.Money, MoneyInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Money) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Money>(parent, R.layout.item_money) {

        private lateinit var field: FieldUi.Money
        private val amountView = itemView as AmountInputLayout

        init {
            amountView.onAmountChanged = { onMoneyChanged(field, it, amountView.currency) }
            amountView.onCurrencyChangedListener = { onMoneyChanged(field, amountView.amount, it) }
        }

        override fun bind(data: FieldUi.Money) {
            this.field = data
            amountView.helperText = data.helperText
            amountView.errorText = data.errorText
            amountView.amount = data.amount
            amountView.currency = data.currency
        }
    }

    private companion object {
        const val ID_TAG = "MoneyInputItemController"
    }
}