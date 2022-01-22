package ru.arzonpay.android.ui.view.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import androidx.annotation.AttrRes
import ru.surfstudio.android.utilktx.util.SdkUtils

private const val DEFAULT_VIBRATION_DURATION = 300L

/**
 * Подать кратковременную вибрацию
 */
@SuppressLint("NewApi", "MissingPermission")
@Suppress("DEPRECATION")
fun Context.vibrate(duration: Long = DEFAULT_VIBRATION_DURATION) {
    val v = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (SdkUtils.isAtLeastOreo()) {
        v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        v.vibrate(duration)
    }
}

fun Context.getThemeColor(@AttrRes id: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}
