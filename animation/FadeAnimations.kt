package ru.arzonpay.android.ui.animation

import ru.arzonpay.android.base_feature.R
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations

/**
 * Набор анимаций Fade In/Out для переключения фрагментов.
 */
object FadeAnimations : BaseResourceAnimations(
    enterAnimation = R.anim.fade_in_fast,
    exitAnimation = R.anim.fade_out_fast,
    popEnterAnimation = R.anim.fade_in_fast,
    popExitAnimation = R.anim.fade_out_fast
)
