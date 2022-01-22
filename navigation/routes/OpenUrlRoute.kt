package ru.arzonpay.android.ui.navigation.routes

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Роут для открытия урлов на браузере
 */
class OpenUrlRoute(private val url: String) : ActivityRoute() {

    override fun createIntent(context: Context): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}
