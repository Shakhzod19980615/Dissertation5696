package ru.arzonpay.android.ui.mvi.save_state

import android.os.Bundle
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.arzonpay.android.ui.mvi.save_state.SaveStateEvent
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * Middleware для шаринга состояния при смене конфигурации.
 *
 * Работает на основе [SaveStateEvent].
 * * [SaveStateEvent.Save] служит для шаринга состояния при его сохранении (например, при вытеснении экрана в фон)
 * * [SaveStateEvent.Restore] служит для восстановления состояния (например, при создании экрана)
 */
@PerScreen
class SaveStateMiddleware @Inject constructor(
    baseMiddlewareDependency: BaseMiddlewareDependency,
    eventDelegateManager: ScreenEventDelegateManager
) : BaseMiddleware<SaveStateEvent>(baseMiddlewareDependency), OnSaveStateDelegate,
    OnRestoreStateDelegate {

    private val stateRelay = PublishRelay.create<SaveStateEvent>()

    init {
        eventDelegateManager.registerDelegate(this)
    }

    override fun onSaveState(outState: Bundle?) {
        stateRelay.accept(SaveStateEvent.Save(outState))
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        stateRelay.accept(SaveStateEvent.Restore(savedInstanceState))
    }

    override fun transform(eventStream: Observable<SaveStateEvent>): Observable<out SaveStateEvent> =
        stateRelay
}
