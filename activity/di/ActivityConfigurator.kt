package ru.arzonpay.android.ui.activity.di

import android.content.Context
import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator
import ru.arzonpay.android.application.app.di.AppComponent
import ru.arzonpay.android.application.app.di.AppInjector

class ActivityConfigurator(
        val context: Context
) : BaseActivityConfigurator<ActivityComponent, AppComponent>() {

    override fun createActivityComponent(parentComponent: AppComponent?): ActivityComponent =
            ru.arzonpay.android.ui.activity.di.DaggerActivityComponent.builder()
                    .appComponent(parentComponent)
                    .build()

    override fun getParentComponent(): AppComponent = AppInjector.appComponent
}