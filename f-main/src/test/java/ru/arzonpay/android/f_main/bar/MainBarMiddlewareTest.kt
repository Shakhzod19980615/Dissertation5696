package ru.arzonpay.android.f_main.bar

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.types.shouldBeInstanceOf
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute
import ru.arzonpay.android.base.test.util.forAll
import ru.arzonpay.android.f_main.bar.MainBarEvent.TabSelected
import ru.arzonpay.android.ui.navigation.routes.FeedFragmentRoute
import ru.arzonpay.android.ui.navigation.routes.MainTabType
import ru.arzonpay.android.ui.navigation.routes.MainTabType.FEED
import ru.arzonpay.android.ui.navigation.routes.MainTabType.PROFILE
import ru.arzonpay.android.ui.navigation.routes.MainTabType.SEARCH
import ru.arzonpay.android.ui.navigation.routes.ProfileFragmentRoute
import ru.arzonpay.android.ui.navigation.routes.SearchFragmentRoute
import ru.arzonpay.android.ui.test.base.BaseMiddlewareTest
import ru.arzonpay.android.ui.test.matcher.shouldBeNavigationCommand
import kotlin.reflect.KClass

internal class MainBarMiddlewareTest : BaseMiddlewareTest() {

    private val sh = MainBarStateHolder()

    @Test
    fun `when tab of given type selected, corresponding tab screen should be opened`() = forAll(
        FEED to FeedFragmentRoute::class,
        PROFILE to ProfileFragmentRoute::class,
        SEARCH to SearchFragmentRoute::class
    ) { tabType: MainTabType, routeClass: KClass<out TabHeadRoute> ->
        val middleware = createMiddleware()
        val inputEvent = TabSelected(tabType)

        val testObserver = middleware.transform(inputEvent.toObservable()).test()

        assertSoftly(testObserver.values().firstOrNull()) {
            shouldBeInstanceOf<MainBarEvent.Navigation>()
                .event
                .shouldBeNavigationCommand<Replace>()
                .route
                .should { route -> route::class.shouldBeSameInstanceAs(routeClass) }
        }
    }

    private fun createMiddleware(): MainBarMiddleware {
        return MainBarMiddleware(baseMiddlewareDependency, sh, navigationMiddleware)
    }
}