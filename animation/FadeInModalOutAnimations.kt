package ru.arzonpay.android.ui.animation

import ru.arzonpay.android.base_feature.R
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations

/**
 * Анимации переключения экранов.
 * Открытие экрана происходит с анимацией fade-in, закрытие - с модальной анимацией.
 */
object FadeInModalOutAnimations : BaseResourceAnimations(
    enterAnimation = R.anim.fade_in_fast,
    exitAnimation = R.anim.fade_out_fast,
    popEnterAnimation = R.anim.fade_in_fast,
    popExitAnimation = R.anim.slide_out_to_bottom
)
