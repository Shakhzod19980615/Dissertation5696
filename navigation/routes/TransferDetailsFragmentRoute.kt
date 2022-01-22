package ru.arzonpay.android.ui.navigation.routes

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.arzonpay.android.domain.payment.Transaction
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class TransferDetailsFragmentRoute(
    val transaction: Transaction
) : FragmentRoute() {

    constructor(args: Bundle) : this(
        args[Route.EXTRA_FIRST] as Transaction
    )

    override fun prepareData() = bundleOf(
        Route.EXTRA_FIRST to transaction
    )

    override fun getScreenClassPath() = "ru.arzonpay.android.f_details.TransferDetailsFragmentView"
}