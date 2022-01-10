package ru.arzonpay.android.f_form.selector

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.ui.dialog.base.result.BaseResultDialogRoute
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_FIRST
import ru.surfstudio.android.navigation.route.Route.Companion.EXTRA_SECOND
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

internal class SelectorBottomSheetRoute(
    val fieldId: String,
    val fieldValues: List<FieldValue>
) : BaseResultDialogRoute<SelectorResult>() {

    @Suppress("UNCHECKED_CAST")
    constructor(args: Bundle) : this(
        args[EXTRA_FIRST] as String,
        args[EXTRA_SECOND] as List<FieldValue>
    )

    override val dialogId: String = EMPTY_STRING

    override fun getScreenClass() = SelectorBottomSheetDialog::class.java

    override fun prepareData() = bundleOf(
        EXTRA_FIRST to fieldId,
        EXTRA_SECOND to fieldValues
    )
}