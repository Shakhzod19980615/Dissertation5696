package ru.arzonpay.android.ui.mvi.navigation.event

import ru.arzonpay.android.ui.mvi.composition.SingleEventComposition

/**
 * Композиция событий навигации на основе команд
 */
interface NavCommandsComposition : SingleEventComposition<NavCommandsEvent>
