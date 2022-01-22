package ru.arzonpay.android.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

class FeedFragmentRoute : FragmentRoute(), TabHeadRoute {

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_feed.FeedFragmentView"
    }
}