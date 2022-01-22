package ru.arzonpay.android.ui.dialog.base.result

import ru.arzonpay.android.ui.dialog.base.CoreSimpleDialog
import java.io.Serializable

/**
 * Базовый интерфейс диалогов, возвращающих результат
 */
interface BaseResultDialogInterface<T : Serializable> : CoreSimpleDialog {

    var result: T?

    val route: BaseResultDialogRoute<T>

    fun dismiss()

    fun closeWithResult(result: T?) {
        this.result = result
        dismiss()
    }
}
