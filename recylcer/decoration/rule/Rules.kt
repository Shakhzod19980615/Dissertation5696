package ru.arzonpay.android.ui.recylcer.decoration.rule

/**
 * Правила для отрисовки разделителей между элементами RecyclerView.
 */
object Rules {

    const val START = 1
    const val MIDDLE = 2
    const val END = 4

    fun checkMiddleRule(rule: Int): Boolean {
        return rule and MIDDLE != 0
    }

    fun checkEndRule(rule: Int): Boolean {
        return rule and END != 0
    }

    fun checkStartRule(rule: Int): Boolean {
        return rule and START != 0
    }

    fun checkAllRule(rule: Int): Boolean {
        return rule and (START or MIDDLE or END) != 0
    }
}
