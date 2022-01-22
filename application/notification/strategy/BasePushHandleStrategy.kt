package ru.arzonpay.android.application.notification.strategy

import android.app.Activity
import android.content.Context
import android.content.Intent
import ru.arzonpay.android.application.notification.type.NotificationTypeData
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.navigation.routes.MainActivityRoute
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy

/**
 * Базовая стратегия обработки push-уведомления
 */
open class BasePushHandleStrategy : SimpleAbstractPushHandleStrategy<NotificationTypeData>() {

    override val typeData by lazy { NotificationTypeData() }

    override val channelId: Int
        get() = R.string.notification_channel_id

    override val channelName: Int
        get() = R.string.notification_channel_name

    override val icon: Int
        get() = R.drawable.ic_notification

    override val color: Int
        get() = R.color.han_purple

    override fun coldStartIntent(context: Context): Intent =
        MainActivityRoute().createIntent(context)

    override fun handlePushInActivity(activity: Activity) = false

    open fun coldStartRoute(): ActivityRoute {
        return MainActivityRoute()
    }
}