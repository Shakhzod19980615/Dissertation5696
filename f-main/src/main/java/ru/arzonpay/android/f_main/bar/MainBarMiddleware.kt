package ru.arzonpay.android.f_main.bar

import io.reactivex.Observable
import ru.arzonpay.android.f_main.bar.MainBarEvent.*
import ru.arzonpay.android.i_push_notification.storage.DeviceInteractor
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.builder
import ru.arzonpay.android.ui.mvi.navigation.extension.finish
import ru.arzonpay.android.ui.mvi.navigation.extension.removeLast
import ru.arzonpay.android.ui.mvi.navigation.extension.replace
import ru.arzonpay.android.ui.mvi.save_state.SaveStateEvent
import ru.arzonpay.android.ui.mvi.save_state.SaveStateMiddleware
import ru.arzonpay.android.ui.navigation.routes.FeedFragmentRoute
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.scope.ScreenScopeNavigationProvider
import ru.surfstudio.android.rx.extension.toObservable
import javax.inject.Inject

@PerScreen
internal class MainBarMiddleware @Inject constructor(
    basePresenterDependency: BaseMiddlewareDependency,
    private val navigationMiddleware: NavigationMiddleware,
    private val screenNavigationProvider: ScreenScopeNavigationProvider,
    private val saveStateMiddleware: SaveStateMiddleware,
    private val deviceInteractor: DeviceInteractor
) : BaseMiddleware<MainBarEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<MainBarEvent>): Observable<out MainBarEvent> =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                PersistentState::class decomposeTo saveStateMiddleware,
                PersistentState::class eventMapTo ::manageSavedState,
                BackPressed::class mapTo { onBackPressed() },
                onFirstCreate() eventMap { registerDevice() }
            )
        }

    private fun manageSavedState(event: PersistentState): Observable<out MainBarEvent> {
        return when (val childEvent = event.events.first()) {
            is SaveStateEvent.Save -> {
                childEvent.state?.putInt(MAIN_TAB_KEY, 0)
                skip()
            }

            is SaveStateEvent.Restore -> {
                val savedState = childEvent.state
                if (savedState == null) {
                    // Первое создание экрана
                    openSelectedTab().toObservable()
                } else {
                    // Восстановление после смены конфигурации/смерти процесса
                    val mainTabTypePosition = savedState.getInt(MAIN_TAB_KEY)
                    //TODO доработать когда добавятся табы
                    skip()
                }
            }
        }
    }

    private fun openSelectedTab(): MainBarEvent {
        val tabRoute: FragmentRoute = FeedFragmentRoute()
        return Navigation().builder()
            .replace(tabRoute)
            .build()
    }

    private fun onBackPressed(): MainBarEvent {
        return when {
            hasTabsInStack() -> Navigation().removeLast()
            else -> Navigation().finish()
        }
    }

    private fun hasTabsInStack(): Boolean {
        return getTabFragmentNavigator().backStackEntryCount > 1
    }

    private fun getTabFragmentNavigator(): TabFragmentNavigatorInterface =
        screenNavigationProvider.getFragmentNavigationHolder()
            .fragmentNavigator as TabFragmentNavigatorInterface

    private fun registerDevice(): Observable<out MainBarEvent> {
        return deviceInteractor.safeRegisterDevice()
            .io()
            .asRequestEvent { RegisterDeviceRequest(it) }
    }

    private companion object {
        const val MAIN_TAB_KEY = "main_tab_key"
    }
}
