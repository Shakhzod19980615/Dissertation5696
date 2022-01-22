package ru.arzonpay.android.ui.util

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import ru.surfstudio.android.animations.anim.AnimationUtil

const val ALPHA_OPAQUE = 1f
const val ALPHA_OPAQUE_INT = 255
const val ALPHA_TRANSPARENT = 0f
const val ALPHA_TRANSPARENT_INT = 0

/**
 * Функция запускает [ValueAnimator] по заданным параметрам
 *
 * @param from от какого значения нужно начинать
 * @param to до какого значения нужно продолжать
 * @param duration продолжительность анимации в миллисекундах
 * @param startDelay задержка до начала анимации
 * @param onUpdate функция которая вызовется при каждом обновлении значения
 * @param onComplete функция которая вызовется по окончании анимации
 *
 * @return запущенную анимацию
 */
inline fun changeValue(
    from: Int,
    to: Int,
    duration: Long,
    startDelay: Long = 0L,
    crossinline onUpdate: (value: Int) -> Unit,
    crossinline onComplete: () -> Unit = {}
): ValueAnimator {
    val vH: PropertyValuesHolder = PropertyValuesHolder.ofInt("prop", from, to)

    return ValueAnimator.ofPropertyValuesHolder(vH).apply {
        this.duration = duration
        this.startDelay = startDelay
        addUpdateListener {
            onUpdate(this.getAnimatedValue("prop") as Int)
        }
        doOnEnd { onComplete() }
        start()
    }
}

/**
 * @see changeValue
 */
inline fun changeValue(
    from: Float,
    to: Float,
    duration: Long,
    startDelay: Long = 0L,
    crossinline update: (value: Float) -> Unit,
    crossinline complete: () -> Unit = {}
): ValueAnimator {
    val vH: PropertyValuesHolder = PropertyValuesHolder.ofFloat("prop", from, to)

    return ValueAnimator.ofPropertyValuesHolder(vH).apply {
        this.duration = duration
        this.startDelay = startDelay
        addUpdateListener {
            update(this.getAnimatedValue("prop") as Float)
        }
        doOnEnd { complete() }
        start()
    }
}

/**
 * Смена двух вью с эффектом fadeIn/fadeOut
 */
fun crossFadeViews(
    inView: View,
    outView: View,
    duration: Long = AnimationUtil.ANIM_LARGE_TRANSITION,
    visibility: Int = View.GONE,
    endAction: (() -> Unit)? = null
) {
    AnimationUtil.fadeIn(inView, duration, ALPHA_OPAQUE, endAction)
    AnimationUtil.fadeOut(outView, duration, visibility, ALPHA_TRANSPARENT, endAction)
}
