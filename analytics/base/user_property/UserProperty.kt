package ru.arzonpay.android.ui.analytics.base.user_property

import ru.surfstudio.android.analyticsv2.core.AnalyticAction

/**
 * Интерфейс сущности, инкапсулирующей данные о свойствах пользователя.
 */
interface UserProperty : AnalyticAction {

    val key: String

    val value: String
}
