package ru.arzonpay.android.ui.util

import android.os.Build

/** Утилитный класс для проверки девайса на специфичные случаи. */
object DeviceSpecificUtil {

    /** Флаг, проверяющий, имеет ли устройство встроенную кастомную цифровую клавиатуру. */
    val isDeviceWithCustomDigitalKeyboard: Boolean by lazy {
        val manufacturer = Build.MANUFACTURER
        manufacturersWithCustomDigitalKeyboard.any { it.equals(manufacturer, ignoreCase = true) }
    }

    /**
     * Список производителей девайсов, у которых дефолтная цифровая клавиатура
     * содержит переход на буквы.
     *
     * Для таких девайсов установки inputType="number" для EditText недостаточно.
     * (см. расширение [EditText.setNumericKeyboardForAllDevices()]
     */
    private val manufacturersWithCustomDigitalKeyboard = listOf(
        "xiaomi",
        "huawei"
    )
}
