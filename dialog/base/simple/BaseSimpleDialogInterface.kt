package ru.arzonpay.android.ui.dialog.base.simple

import ru.arzonpay.android.ui.dialog.base.CoreSimpleDialog

/**
 * Базовый интерфейс диалогов, возвращающих простой результат
 */
interface BaseSimpleDialogInterface : CoreSimpleDialog {

    var result: SimpleResult

    val route: BaseSimpleDialogRoute

    fun dismiss()

    fun closeWithResult(result: SimpleResult) {
        this.result = result
        dismiss()
    }
}
