package ru.arzonpay.android.ui.util

import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SwipeRefreshLoading

/** Extension-флаг: "выполняется ли в данный момент загрузка через SWR?". */
val <T> RequestUi<T>.isSwrLoading: Boolean
    get() = loadStateTypeOrNull?.isSwrLoading ?: false

/**
 * Extension-поле для получения [LoadStateType] этого запроса.
 *
 * Если [LoadStateType] присутствует в этом запросе,
 * то он будет возвращен, иначе будет возвращен `null`.
 */
val RequestUi<*>.loadStateTypeOrNull: LoadStateType?
    get() = load as? LoadStateType

/**
 * Extension-поле для получения [LoadStateType] этого запроса.
 *
 * Если [LoadStateType] присутствует в этом запросе,
 * то он будет возвращен, иначе будет выкинут [ClassCastException].
 */
val RequestUi<*>.loadStateType: LoadStateType
    get() = load as LoadStateType

/**
 * Extension-метод для получения [LoadStateType] этого запроса.
 *
 * Если [LoadStateType] присутствует в этом запросе,
 * то он будет возвращен, иначе будет возвращен [default].
 */
fun RequestUi<*>.loadStateTypeOrDefault(default: LoadStateType = LoadStateType.None()): LoadStateType {
    return loadStateTypeOrNull ?: default
}
