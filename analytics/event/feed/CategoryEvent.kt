package ru.arzonpay.android.ui.analytics.event.feed

import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class CategoryEvent(
    val id: String
) : CommonAnalyticsEvent {

    override fun key() = "category"

    override fun params() = bundleOf(
        "id" to id
    )
}