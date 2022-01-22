package ru.arzonpay.android.ui.util.mvi

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import java.util.concurrent.TimeUnit

const val CLICK_DELAY = 1000L

/**
 * Функция-расширение для создание [Observable] из списка событий типа [T].
 */
fun <T : Event> RxMiddleware<T>.concat(vararg events: T): Observable<out T> =
    Observable.fromIterable(events.toList())

/**
 * Функция-расширение для добавления throttle-эффекта цепочке [Observable].
 *
 * @param delay задержка в миллисекундах.
 */
infix fun <T : Event> Observable<T>.withThrottle(delay: Long): Observable<T> {
    return throttleFirst(delay, TimeUnit.MILLISECONDS)
}
