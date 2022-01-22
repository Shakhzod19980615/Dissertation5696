package ru.arzonpay.android.ui.resource

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.arzonpay.android.ui.util.LocaleChanger
import ru.surfstudio.android.dagger.scope.PerApplication
import java.util.*
import javax.inject.Inject

@PerApplication
class LocaleContextProvider @Inject constructor(
    settingsInteractor: SettingsInteractor,
    private var context: Context
) {

    init {
        update(Locale(settingsInteractor.getLanguage().code))
//        val disposable = settingsInteractor.languageChangedObservable
//            .subscribe {
//                update(Locale(it.code))
//            }
    }

    fun get(): Context {
        return context
    }

    private fun update(locale: Locale) {
        context = LocaleChanger.updateConfiguration(context, locale)
    }
}