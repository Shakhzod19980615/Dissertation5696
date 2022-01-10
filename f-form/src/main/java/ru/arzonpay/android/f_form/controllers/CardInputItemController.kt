package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.ItemCardBinding
import ru.arzonpay.android.ui.util.CARD_MASK
import ru.arzonpay.android.ui.util.installMask
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class CardInputItemController(
    private val onCardChanged: (field: FieldUi.Card, formatted: String, extracted: String) -> Unit
) : BindableItemController<FieldUi.Card, CardInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Card) = "$ITEM_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Card>(parent, R.layout.item_card) {

        private lateinit var field: FieldUi.Card
        private val binding = ItemCardBinding.bind(itemView)

        init {
            initCardFormatter()
        }

        override fun bind(data: FieldUi.Card) {
            this.field = data
            binding.cardNumberTil.hint = data.hint
            binding.cardNumberTil.helperText = data.helperText
            binding.cardNumberTil.errorText = data.errorText
            binding.cardNumberEt.distinctText = data.value
        }

        private fun initCardFormatter() {
            installMask(CARD_MASK, binding.cardNumberEt) { extractedValue, formattedValue ->
                onCardChanged(field, formattedValue, extractedValue)
            }
        }
    }

    private companion object {
        const val ITEM_TAG = "CartInputItemController"
    }
}