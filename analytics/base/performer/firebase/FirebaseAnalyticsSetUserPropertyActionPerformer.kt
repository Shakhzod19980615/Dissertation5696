package ru.arzonpay.android.ui.analytics.base.performer.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import ru.arzonpay.android.ui.analytics.base.user_property.FirebaseAnalyticsUserProperty
import ru.surfstudio.android.analyticsv2.core.AnalyticAction
import ru.surfstudio.android.analyticsv2.core.AnalyticActionPerformer
import ru.surfstudio.android.firebaseanalytics.api.MAX_SET_USER_PROPERTY_KEY_LENGTH
import ru.surfstudio.android.firebaseanalytics.api.MAX_SET_USER_PROPERTY_VALUE_LENGTH

/**
 * Сущность, выполняющая действия `setUserProperty` для FirebaseAnalytics.
 */
class FirebaseAnalyticsSetUserPropertyActionPerformer(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticActionPerformer<FirebaseAnalyticsUserProperty> {

    override fun canHandle(action: AnalyticAction) = action is FirebaseAnalyticsUserProperty

    override fun perform(action: FirebaseAnalyticsUserProperty) {
        firebaseAnalytics.setUserProperty(
            action.key.take(MAX_SET_USER_PROPERTY_KEY_LENGTH),
            action.value.take(MAX_SET_USER_PROPERTY_VALUE_LENGTH)
        )
    }
}
