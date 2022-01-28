package ru.arzonpay.android.f_details.controller

import android.view.ViewGroup
import ru.arzonpay.android.f_details.R
import ru.arzonpay.android.f_details.data.FieldUi
import ru.arzonpay.android.f_details.databinding.ItemTransferAmountBinding
import ru.arzonpay.android.ui.util.getIcon
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class TransferAmountItemController :
    BindableItemController<FieldUi.Amount, TransferAmountItemController.Holder>() {

    override fun getItemId(data: FieldUi.Amount) = "$ID_TAG${data.title}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Amount>(parent, R.layout.item_transfer_amount) {

        private val binding = ItemTransferAmountBinding.bind(itemView)

        override fun bind(data: FieldUi.Amount) = with(binding) {
            valueTsv.titleText = data.title
            valueTsv.subTitleText = data.value
            currencyIv.setImageResource(data.currency.getIcon())
        }
    }

    private companion object {
        const val ID_TAG = "TransferMoneyItemController"
    }
}