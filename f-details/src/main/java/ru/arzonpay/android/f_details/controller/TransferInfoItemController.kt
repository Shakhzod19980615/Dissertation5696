package ru.arzonpay.android.f_details.controller

import android.view.ViewGroup
import ru.arzonpay.android.f_details.R
import ru.arzonpay.android.f_details.data.FieldUi
import ru.surfstudio.android.custom.view.TitleSubtitleView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

internal class TransferInfoItemController :
    BindableItemController<FieldUi.Info, TransferInfoItemController.Holder>() {

    override fun getItemId(data: FieldUi.Info) = "$ID_TAG${data.title}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) :
        BindableViewHolder<FieldUi.Info>(parent, R.layout.item_transfer_info) {

        private val infoTsv = itemView as TitleSubtitleView

        override fun bind(data: FieldUi.Info) = with(infoTsv) {
            titleText = data.title
            subTitleText = data.value
        }
    }

    private companion object {
        const val ID_TAG = "TransferInfoItemController"
    }
}