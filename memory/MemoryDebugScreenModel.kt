package ru.arzonpay.android.f_debug.memory

import ru.surfstudio.android.core.mvp.model.ScreenModel

class MemoryDebugScreenModel(
        val isLeakCanaryEnabled: Boolean
) : ScreenModel()