package ru.arzonpay.android.ui.animation

import ru.arzonpay.android.base_feature.R
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations

/**
 * Набор анимаций для модального появления/скрытия экрана
 */
object ModalAnimations : BaseResourceAnimations(
    enterAnimation = R.anim.slide_in_from_bottom,
    exitAnimation = R.anim.fade_out_fast,
    popEnterAnimation = R.anim.fade_in_fast,
    popExitAnimation = R.anim.slide_out_to_bottom
)
