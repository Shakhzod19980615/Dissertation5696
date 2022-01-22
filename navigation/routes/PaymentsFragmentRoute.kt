package ru.arzonpay.android.ui.navigation.routes

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.arzonpay.android.domain.payment.Provider
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FIRST
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class PaymentsFragmentRoute(
    val parent: Provider
) : FragmentRoute() {

    constructor(args: Bundle) : this(
        args[EXTRA_FIRST] as Provider
    )

    override fun prepareData() = bundleOf(
        EXTRA_FIRST to parent
    )

    override fun getId(): String {
        return super.getId() + parent.id
    }

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_payments.PaymentsFragmentView"
    }
}