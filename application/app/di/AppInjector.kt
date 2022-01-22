package ru.arzonpay.android.application.app.di

import android.content.Context
import ru.arzonpay.android.application.app.App

/**
 * Объект ответственный за создание и хранение [AppComponent]
 */
object AppInjector {

    lateinit var appComponent: AppComponent

    fun initInjector(app: App, context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app, context, app.activeActivityHolder))
            .build()
    }
}