package ru.arzonpay.android.f_auth.code

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FIRST
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_SECOND
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

internal class SmsCodeFragmentRoute(
    val phone: String,
    val key: String
) : FragmentRoute() {

    constructor(args: Bundle) : this(
        args[EXTRA_FIRST] as String,
        args[EXTRA_SECOND] as String
    )

    override fun getScreenClass() = SmsCodeFragmentView::class.java

    override fun prepareData(): Bundle {
        return bundleOf(
            EXTRA_FIRST to phone,
            EXTRA_SECOND to key
        )
    }
}