package ru.arzonpay.android.ui.dialog.base.result

import android.content.DialogInterface
import androidx.annotation.CallSuper
import java.io.Serializable
import ru.surfstudio.android.mvp.dialog.simple.bottomsheet.CoreSimpleBottomSheetDialogFragment
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult

/**
 * Базовый простой bottom sheet диалог с возможностью возвращения результата.
 *
 * @property result результат работы экрана. Эмитится при его закрытии.
 * @property route route, служащая для открытия экрана, и для оповещения о результате.
 */
abstract class BaseResultBottomDialogView<T : Serializable> : CoreSimpleBottomSheetDialogFragment(),
    BaseResultDialogInterface<T> {

    override var result: T? = null

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        result?.let {
            getCommandExecutor().execute(EmitScreenResult(route, it))
        }
        super.onDismiss(dialog)
    }
}
