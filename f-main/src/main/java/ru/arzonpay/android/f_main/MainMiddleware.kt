package ru.arzonpay.android.f_main

import io.reactivex.Observable
import ru.arzonpay.android.f_main.MainEvent.Navigation
import ru.arzonpay.android.i_auth.AuthInteractor
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.builder
import ru.arzonpay.android.ui.mvi.navigation.extension.replace
import ru.arzonpay.android.ui.navigation.data.Destination
import ru.arzonpay.android.ui.navigation.routes.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
internal class MainMiddleware @Inject constructor(
    basePresenterDependency: BaseMiddlewareDependency,
    private val navigationMiddleware: NavigationMiddleware,
    private val authInteractor: AuthInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val mainActivityRoute: MainActivityRoute
) : BaseMiddleware<MainEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<MainEvent>): Observable<out MainEvent> =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                onFirstCreate() map { openNextRoute() },
                observeLanguageChanges()
            )
        }

    private fun openNextRoute(): MainEvent {
        val nextRoute = when {
            !settingsInteractor.isLanguageChosen() -> LanguageFragmentRoute()
            !authInteractor.isAuthorized -> AuthFragmentRoute()
            else -> MainBarRoute()
        }
        val destination = mainActivityRoute.destination
        return if (authInteractor.isAuthorized && destination is Destination.TransactionScreen) {
            Navigation().builder()
                .replace(nextRoute)
                .replace(TransferDetailsFragmentRoute(destination.transaction))
                .build()
        } else {
            Navigation().replace(nextRoute)
        }
    }

    private fun observeLanguageChanges(): Observable<out MainEvent> {
        return settingsInteractor.languageChangedObservable
            .debounce(500L, TimeUnit.MILLISECONDS)
            .map { MainEvent.LanguageChanged(it) }
    }
}
