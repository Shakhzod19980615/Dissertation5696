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

internal class MultilineInputItemController(
    private val onValueChanged: (field: FieldUi.Multiline, value: String) -> Unit
) : BindableItemController<FieldUi.Multiline, MultilineInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Multiline) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Multiline>(parent, R.layout.item_multiline) {

        private lateinit var field: FieldUi.Multiline
        private val binding = ItemMultilineBinding.bind(itemView)

        init {
            binding.inputEt.setOnTextChanged {
                onValueChanged(field, it)
            }
        }

        override fun bind(data: FieldUi.Multiline) {
            this.field = data
            binding.inputTil.hint = data.hint
            binding.inputTil.helperText = data.helperText
            binding.inputTil.errorText = data.errorText
            binding.inputEt.distinctText = data.value
        }
    }

    private companion object {
        const val ID_TAG = "MultilineInputItemController"
    }
}