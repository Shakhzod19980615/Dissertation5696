package ru.arzonpay.android.ui.dialog.base.result

import java.io.Serializable
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Базовый класс роута простого диалога с результатом типа [T].
 */
abstract class BaseResultDialogRoute<T : Serializable> : DialogRoute(), ResultRoute<T> {

    /**
     * Идентификатор диалога, используется чтобы не возникало коллизий
     * при получении результатов диалогов запущенных на разных экранах
     */
    protected abstract val dialogId: String

    override fun getId(): String {
        return super.getId() + dialogId
    }
}
