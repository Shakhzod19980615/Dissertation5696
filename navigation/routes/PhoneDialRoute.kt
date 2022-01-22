package ru.arzonpay.android.ui.navigation.routes

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class PhoneDialRoute(
    private val phone: String
) : ActivityRoute() {

    override fun createIntent(context: Context): Intent {
        return Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phone}")
        }
    }
}