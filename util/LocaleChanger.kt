package ru.arzonpay.android.ui.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import java.util.*

object LocaleChanger {

    @Suppress("DEPRECATION")
    fun updateConfiguration(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val resources = context.resources
        val newConfig = Configuration(resources.configuration)
        newConfig.setLocale(locale)
        newConfig.setLayoutDirection(locale)
        return if (SDK_INT >= N)
            context.createConfigurationContext(newConfig)
        else resources.updateConfiguration(newConfig, resources.displayMetrics).let { context }
    }
}
