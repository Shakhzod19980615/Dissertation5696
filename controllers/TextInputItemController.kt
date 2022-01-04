package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.ItemMultilineBinding
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.arzonpay.android.ui.view.extensions.setOnTextChanged
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class TextInputItemController(
    private val onValueChanged: (field: FieldUi.Text, value: String) -> Unit
) : BindableItemController<FieldUi.Text, TextInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Text) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Text>(parent, R.layout.item_text) {

        private lateinit var field: FieldUi.Text
        private val binding = ItemMultilineBinding.bind(itemView)

        init {
            binding.inputEt.setOnTextChanged {
                onValueChanged(field, it)
            }
        }

        override fun bind(data: FieldUi.Text) {
            this.field = data
            binding.inputTil.hint = data.hint
            binding.inputTil.helperText = data.helperText
            binding.inputTil.errorText = data.errorText
            binding.inputEt.distinctText = data.value
        }
    }

    private companion object {
        const val ID_TAG = "TextInputItemController"
    }
}