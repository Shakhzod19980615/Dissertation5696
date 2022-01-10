package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.ItemDigitsBinding
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.arzonpay.android.ui.view.extensions.setOnTextChanged
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class DigitInputItemController(
    private val onValueChanged: (field: FieldUi.Digits, value: String) -> Unit
) : BindableItemController<FieldUi.Digits, DigitInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Digits) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Digits>(parent, R.layout.item_digits) {

        private lateinit var field: FieldUi.Digits
        private val binding = ItemDigitsBinding.bind(itemView)

        init {
            binding.inputEt.setOnTextChanged {
                onValueChanged(field, it)
            }
        }

        override fun bind(data: FieldUi.Digits) {
            this.field = data
            binding.inputTil.hint = data.hint
            binding.inputTil.helperText = data.helperText
            binding.inputTil.errorText = data.errorText
            binding.inputEt.distinctText = data.value
        }
    }

    private companion object {
        const val ID_TAG = "DigitInputItemController"
    }
}