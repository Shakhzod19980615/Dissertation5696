package ru.arzonpay.android.f_form.selector.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.f_form.R
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class ValueItemController(
    private val onClick: (FieldValue) -> Unit
) : BindableItemController<FieldValue, ValueItemController.Holder>() {

    override fun getItemId(data: FieldValue) = "$ID_TAG${data.id}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldValue>(parent, R.layout.item_field_value) {

        private lateinit var value: FieldValue
        private val valueTv = itemView as TextView

        init {
            valueTv.setOnClickListener { onClick(value) }
        }

        override fun bind(data: FieldValue) {
            value = data
            valueTv.text = data.value
        }
    }

    private companion object {
        const val ID_TAG = "ValueItemController"
    }
}