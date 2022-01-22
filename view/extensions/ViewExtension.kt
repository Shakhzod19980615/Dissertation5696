package ru.arzonpay.android.ui.view.extensions

import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.*
import ru.surfstudio.android.animations.anim.AnimationUtil
import ru.surfstudio.android.animations.anim.fadeIn
import ru.surfstudio.android.animations.anim.fadeOut
import ru.surfstudio.android.utilktx.ktx.ui.view.actionIfChanged

/**
 * Выполнить действие [action], если [data] была изменена с момента последнего вызова этого метода.
 *
 * Если `data == null` или `data == previous_data` -> [action] не будет вызван.
 * */
fun <T : Any, V : View> V.performIfChanged(data: T?, action: V.(T) -> Unit) {
    actionIfChanged(data, { if (data != null) action(data) })
}

/**
 * см. [performIfChanged]
 */
fun <T1 : Any, T2 : Any, V : View> V.performIfChanged(
    data1: T1?,
    data2: T2?,
    action: V.(T1, T2) -> Unit
) {
    actionIfChanged(
        data1,
        data2,
        { _, _ -> if (data1 != null && data2 != null) action(data1, data2) })
}

/**
 * см. [performIfChanged]
 */
fun <T1 : Any, T2 : Any, T3 : Any, V : View> V.performIfChanged(
    data1: T1?,
    data2: T2?,
    data3: T3?,
    action: V.(T1, T2, T3) -> Unit
) {
    actionIfChanged(
        data1,
        data2,
        { _, _ ->
            if (data1 != null && data2 != null && data3 != null) action(
                data1,
                data2,
                data3
            )
        })
}

/**
 * Плавно скрывающая или показывающая View анимация.
 *
 * @param condition условие, в зависимости от которого view будет показана или скрыта
 */
fun View.fadeOutIf(
    condition: Boolean,
    duration: Long = AnimationUtil.ANIM_LEAVING,
    defaultAlpha: Float = 1.0f,
    endAction: (() -> Unit)? = null
) {
    if (condition) {
        if (visibility != View.GONE) {
            fadeOut(defaultAlpha = defaultAlpha, duration = duration, endAction = endAction)
        }
    } else {
        if (visibility != View.VISIBLE) {
            fadeIn(defaultAlpha = defaultAlpha, duration = duration, endAction = endAction)
        }
    }
}

/**
 * Свойство-расширение [View] для получения [DisplayMetrics].
 */
val View.displayMetrics: DisplayMetrics
    get() = resources.displayMetrics

/**
 * Изменение видимости [View] в случае, если [isVisible] и [newIsVisible] не совпадают.
 */
fun View.trySetVisible(newIsVisible: Boolean) {
    val currentIsVisible = isVisible
    if (currentIsVisible != newIsVisible) {
        isVisible = newIsVisible
    }
}

/**
 * Функция-расширение для удобного получения размеров в px (`PixelSize`) внутри [View].
 */
fun View.getDimensionPixelSize(@DimenRes dimenResId: Int) =
    resources.getDimensionPixelSize(dimenResId)

/** Функция-расширение для удобного получения размеров в px (`PixelOffset`) внутри [View].  */
fun View.getDimen(@DimenRes resId: Int) =
    resources.getDimensionPixelOffset(resId)

/** Функция-расширение для удобного получения размеров в px (`PixelSize`) внутри [View]. */
fun View.getDimenF(@DimenRes resId: Int): Float =
    getDimensionPixelSize(resId).toFloat()

/**
 * Функция-расширение для удобного получения цвета из ресурсов внутри [View].
 *
 * @return цвет в формате 0xAARRGGBB.
 */
fun View.getColor(@AttrRes colorRes: Int): Int {
    return context.getThemeColor(colorRes)
}

/**
 * Функция-расширение для удобного получения целого числа из ресурсов внутри [View].
 */
fun View.getInteger(@IntegerRes id: Int): Int =
    context.resources.getInteger(id)

/**
 * Функция-расширение для удобного получения [Drawable] из ресурсов внутри [View].
 */
fun View.getDrawable(@DrawableRes drawableRes: Int): Drawable? =
    context.getDrawable(drawableRes)

/**
 * Функция-расширение для удобного получения строки из ресурсов внутри [View].
 */
fun View.getString(@StringRes stringRes: Int, vararg formatArgs: Any): String =
    context.resources.getString(stringRes, *formatArgs)

/**
 * Функция-расширение для удобного получения [Drawable] из ресурсов внутри [View].
 * К полученному [Drawable] будет применен цветофильтр [color].
 */
fun View.getTintedDrawable(
    @DrawableRes drawableRes: Int,
    @ColorInt color: Int
): Drawable? = getDrawable(drawableRes)?.apply {
    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
}

/**
 * Поиск всех [View] с заданным тегом.
 */
fun View.findViewsByTag(tag: String): List<View> {
    val views = ArrayList<View>()
    if (this.tag == tag) {
        views.add(this)
    }
    if (this is ViewGroup) {
        children.forEach { child ->
            if (child is ViewGroup) {
                views.addAll(child.findViewsByTag(tag))
            }
            if (child.tag == tag) {
                views.add(child)
            }
        }
    }
    return views
}

/**
 * Extension-метод, который запускает [action] если [data] изменилась.
 *
 * Аналогичен extension-методу [actionIfChanged], но с более чистым api.
 * Не передает в [action] никаких аргументов.
 * */
fun <T : Any> View.performIfChanged(data: T?, action: () -> Unit) {
    actionIfChanged(data) { _ -> action() }
}

/**
 * Метод-расширение для установки одинаковых отступов с нескольких сторон View одновременно.
 */
fun View.setMargin(@Px margin: Int) {
    setMargin(margin, margin, margin, margin)
}

/**
 * Метод-расширение для установки отступов с нескольких сторон View одновременно.
 */
fun View.setMargin(
    @Px left: Int = marginLeft,
    @Px top: Int = marginTop,
    @Px right: Int = marginRight,
    @Px bottom: Int = marginBottom
) {
    val params = layoutParams as? ViewGroup.MarginLayoutParams
    params?.leftMargin = left
    params?.topMargin = top
    params?.rightMargin = right
    params?.bottomMargin = bottom
}

/**
 * Метод-расширение для удобного изменения констрейнтов View, расположенной внутри ConstraintLayout.
 */
inline fun View.updateConstraints(block: ConstraintLayout.LayoutParams.() -> Unit) {
    updateLayoutParams(block)
}

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toSp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()

val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


fun <T> unsafeLazy(action: () -> T) = lazy(LazyThreadSafetyMode.NONE, action)