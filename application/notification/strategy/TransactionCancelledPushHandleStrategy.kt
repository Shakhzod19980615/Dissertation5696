package ru.arzonpay.android.application.notification.strategy

import android.content.Context
import android.content.Intent
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.navigation.data.Destination
import ru.arzonpay.android.ui.navigation.routes.MainActivityRoute
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.notification.interactor.push.PushInteractor

class TransactionCancelledPushHandleStrategy : BasePushHandleStrategy() {

    override fun coldStartIntent(context: Context): Intent {
        val transaction = typeData.data?.transaction
        val destination = if (transaction != null) {
            Destination.TransactionScreen(transaction)
        } else {
            Destination.None
        }
        return MainActivityRoute(destination).createIntent(context)
    }

    override fun coldStartRoute(): ActivityRoute {
        val transaction = typeData.data?.transaction
        val destination = if (transaction != null) {
            Destination.TransactionScreen(transaction)
        } else {
            Destination.None
        }
        return MainActivityRoute(destination)
    }

    override fun handle(
        context: Context,
        pushInteractor: PushInteractor,
        uniqueId: Int,
        title: String,
        body: String
    ) {
        super.handle(
            context,
            pushInteractor,
            uniqueId,
            context.getString(R.string.status_declined),
            context.getString(R.string.notification_transaction_cancelled_body)
        )
    }
}