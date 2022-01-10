package ru.arzonpay.android.f_form.controllers

import android.view.ViewGroup
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.ItemPhoneBinding
import ru.arzonpay.android.ui.util.UZ_PHONE_MASK
import ru.arzonpay.android.ui.util.installMask
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class PhoneInputItemController(
    private val onPhoneChanged: (FieldUi.Phone, formatted: String, extracted: String) -> Unit
) : BindableItemController<FieldUi.Phone, PhoneInputItemController.Holder>() {

    override fun getItemId(data: FieldUi.Phone) = "$ID_TAG${data.name}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Phone>(parent, R.layout.item_phone) {

        private lateinit var field: FieldUi.Phone
        private val binding = ItemPhoneBinding.bind(itemView)

        init {
            initFormatter()
        }

        override fun bind(data: FieldUi.Phone) {
            this.field = data
            binding.phoneEt.distinctText = data.value
            binding.phoneTil.helperText = data.helperText
            binding.phoneTil.errorText = data.errorText
        }

        private fun initFormatter() {
            installMask(UZ_PHONE_MASK, binding.phoneEt) { extractedValue, formattedValue ->
                onPhoneChanged(field, formattedValue, extractedValue)
            }
        }
    }

    private companion object {
        const val ID_TAG = "PhoneInputItemController"
    }
}