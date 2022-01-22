package ru.arzonpay.android.ui.analytics.event.language

import androidx.core.os.bundleOf
import ru.arzonpay.android.domain.locale.Language
import ru.arzonpay.android.ui.analytics.base.event.CommonAnalyticsEvent

class LanguageSelectedEvent(
    private val language: Language
) : CommonAnalyticsEvent {

    override fun key() = "language_selected"

    override fun params() = bundleOf(
        "language_code" to language.code
    )
}