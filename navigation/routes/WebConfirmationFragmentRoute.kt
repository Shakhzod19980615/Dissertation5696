package ru.arzonpay.android.ui.navigation.routes

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.dialog.base.simple.SimpleResult
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

class WebConfirmationFragmentRoute(
    val uuid: String,
    val title: String = EMPTY_STRING
) : FragmentRoute(), ResultRoute<SimpleResult> {

    constructor(args: Bundle) : this(
        args[Route.EXTRA_FIRST] as String,
        args[Route.EXTRA_SECOND] as String
    )

    override fun prepareData() = bundleOf(
        Route.EXTRA_FIRST to uuid,
        Route.EXTRA_SECOND to title
    )

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_confirmation.WebConfirmationFragmentView"
    }
}