package ru.arzonpay.android.ui.navigation.routes

import android.content.Context
import android.content.Intent
import ru.arzonpay.android.ui.navigation.data.Destination
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Роут главного экрана
 */
class MainActivityRoute(
    val destination: Destination = Destination.None
) : ActivityRoute() {

    constructor(intent: Intent) : this(
        intent.getSerializableExtra(Route.EXTRA_FIRST) as? Destination ?: Destination.None
    )

    override fun createIntent(context: Context): Intent {
        return super.createIntent(context).apply {
            putExtra(Route.EXTRA_FIRST, destination)
        }
    }

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_main.MainActivityView"
    }
}