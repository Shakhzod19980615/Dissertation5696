package ru.arzonpay.android.ui.analytics.base.event

import android.os.Bundle
import ru.surfstudio.android.analyticsv2.HasKey
import ru.surfstudio.android.analyticsv2.core.AnalyticAction

/**
 * Событие HuaweiAnalytics.
 */
interface HuaweiAnalyticsEvent : AnalyticAction, HasKey {

    /**
     * Получение параметров события.
     */
    fun params(): Bundle
}
