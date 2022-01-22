package ru.arzonpay.android.ui.analytics.base

import com.google.firebase.analytics.FirebaseAnalytics
import ru.arzonpay.android.ui.analytics.base.performer.firebase.FirebaseAnalyticsSetUserPropertyActionPerformer
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer
import ru.surfstudio.android.firebaseanalytics.api.FirebaseAnalyticEventSender

/**
 * Добавляет поддержку событий logEvent и setUserProperty для FirebaseAnalytics.
 */
fun DefaultAnalyticService.configDefaultFirebaseAnalyticsActions(
    firebaseAnalytics: FirebaseAnalytics
): DefaultAnalyticService {
    addActionPerformer(FirebaseAnalyticEventSender(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    addActionPerformer(FirebaseAnalyticsSetUserPropertyActionPerformer(firebaseAnalytics) as AnalyticActionPerformer<AnalyticAction>)
    return this
}
