package ru.arzonpay.android.ui.navigation.routes

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.arzonpay.android.domain.payment.FormItem
import ru.arzonpay.android.domain.payment.Provider
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class FormFragmentRoute(
    val provider: Provider,
    val items: List<FormItem> = emptyList()
) : FragmentRoute() {

    constructor(args: Bundle) : this(
        args[Route.EXTRA_FIRST] as Provider,
        args[Route.EXTRA_SECOND] as List<FormItem>
    )

    override fun prepareData() = bundleOf(
        Route.EXTRA_FIRST to provider,
        Route.EXTRA_SECOND to items
    )

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_form.FormFragmentView"
    }
}