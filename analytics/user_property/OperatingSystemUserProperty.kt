package ru.arzonpay.android.ui.analytics.user_property

import ru.arzonpay.android.ui.analytics.base.user_property.FirebaseAnalyticsUserProperty
import ru.arzonpay.android.ui.analytics.base.user_property.HuaweiAnalyticsUserProperty

/**
 * User property - операционная система.
 */
object OperatingSystemUserProperty : FirebaseAnalyticsUserProperty, HuaweiAnalyticsUserProperty {

    override val key: String = "os"

    override val value: String = "android"
}
