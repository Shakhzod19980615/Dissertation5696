package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.ItemSelectorBinding
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

internal class SelectorInputItemController(
    private val onSelectorClicked: (FieldUi.Selector) -> Unit
) : BindableItemController<FieldUi.Selector, SelectorInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Selector) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Selector>(parent, R.layout.item_selector) {

        private val binding = ItemSelectorBinding.bind(itemView)
        private lateinit var field: FieldUi.Selector

        init {
            binding.clickerV.setOnClickListener {
                onSelectorClicked(field)
            }
        }

        override fun bind(data: FieldUi.Selector) = with(binding) {
            field = data
            inputTil.errorText = data.errorText
            inputTil.hint = data.hint
            inputEt.distinctText = data.value?.value ?: EMPTY_STRING
        }
    }


    private companion object {
        const val ID_TAG = "SelectorInputItemController"
    }
}