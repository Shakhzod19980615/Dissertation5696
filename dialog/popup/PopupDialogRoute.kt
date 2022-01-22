package ru.arzonpay.android.ui.dialog.popup

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import ru.arzonpay.android.ui.dialog.base.result.BaseResultDialogRoute
import ru.arzonpay.android.ui.dialog.base.simple.SimpleResult
import ru.arzonpay.android.ui.util.EMPTY_RESOURCE
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FIFTH
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FIRST
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FOURTH
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_SECOND
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_SEVEN
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_SIXTH
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_THIRD
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Роут стандартного диалога.
 */
class PopupDialogRoute(
    override val dialogId: String,
    @DrawableRes val iconRes: Int = EMPTY_RESOURCE,
    val titleText: CharSequence = EMPTY_STRING,
    val subtitleText: CharSequence = EMPTY_STRING,
    val primaryButtonText: String = EMPTY_STRING,
    val secondaryButtonText: String = EMPTY_STRING,
    val isCancellable: Boolean = false
) : BaseResultDialogRoute<SimpleResult>() {

    constructor(args: Bundle) : this(
        args[EXTRA_FIRST] as String,
        args[EXTRA_SECOND] as Int,
        args[EXTRA_THIRD] as CharSequence,
        args[EXTRA_FOURTH] as CharSequence,
        args[EXTRA_FIFTH] as String,
        args[EXTRA_SIXTH] as String,
        args[EXTRA_SEVEN] as Boolean,
    )

    override fun prepareData() = bundleOf(
        EXTRA_FIRST to dialogId,
        EXTRA_SECOND to iconRes,
        EXTRA_THIRD to titleText,
        EXTRA_FOURTH to subtitleText,
        EXTRA_FIFTH to primaryButtonText,
        EXTRA_SIXTH to secondaryButtonText,
        EXTRA_SEVEN to isCancellable,
    )

    override fun getScreenClass() = PopupDialogView::class.java
}
