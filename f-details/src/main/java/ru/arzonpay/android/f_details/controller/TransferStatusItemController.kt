package ru.arzonpay.android.f_details.controller

import android.view.ViewGroup
import ru.arzonpay.android.f_details.R
import ru.arzonpay.android.f_details.data.FieldUi
import ru.arzonpay.android.f_details.databinding.ItemTransferStatusBinding
import ru.arzonpay.android.ui.util.context
import ru.arzonpay.android.ui.util.getDrawable
import ru.arzonpay.android.ui.util.getText
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class TransferStatusItemController :
    BindableItemController<FieldUi.Status, TransferStatusItemController.Holder>() {

    override fun getItemId(data: FieldUi.Status) = "$ID_TAG${data.status}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Status>(parent, R.layout.item_transfer_status) {

        private val binding = ItemTransferStatusBinding.bind(itemView)

        override fun bind(data: FieldUi.Status) = with(binding) {
            statusIv.setImageDrawable(data.status.getDrawable(context))
            statusTv.text = data.status.getText(context)
        }
    }

    private companion object {
        const val ID_TAG = "TransferStatusItemController"
    }
}