package ru.arzonpay.android.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class SupportFragmentRoute : FragmentRoute() {

    override fun getScreenClassPath(): String {
        return "ru.arzonpay.android.f_support.SupportFragmentView"
    }
}