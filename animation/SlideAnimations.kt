package ru.arzonpay.android.ui.animation

import ru.arzonpay.android.base_feature.R
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations

/**
 * Набор анимаций Slide In/Out для переключения фрагментов.
 */
object SlideAnimations : BaseResourceAnimations(
    enterAnimation = R.anim.slide_in_right,
    exitAnimation = R.anim.slide_out_left,
    popEnterAnimation = R.anim.slide_in_left,
    popExitAnimation = R.anim.slide_out_right
)
