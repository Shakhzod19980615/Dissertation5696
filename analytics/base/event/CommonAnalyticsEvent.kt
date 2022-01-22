package ru.arzonpay.android.ui.analytics.base.event

import ru.surfstudio.android.firebaseanalytics.api.FirebaseAnalyticEvent

/**
 * Интерфейс для событий, отправляемых одновременно во все платформы сбора аналитики.
 */
interface CommonAnalyticsEvent : FirebaseAnalyticEvent, HuaweiAnalyticsEvent
