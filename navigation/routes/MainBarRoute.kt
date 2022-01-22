package ru.arzonpay.android.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Роут главного экрана с табами
 */
class MainBarRoute : FragmentRoute() {

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_main.bar.MainBarFragmentView"
    }
}
