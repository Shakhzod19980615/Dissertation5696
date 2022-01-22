package ru.arzonpay.android.ui.activity.di

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.arzonpay.android.application.app.di.AppComponent
import ru.arzonpay.android.application.app.di.AppProxyDependencies

/**
 * Компонент для @[PerActivity] скоупа
 */
@PerActivity
@Component(dependencies = [AppComponent::class],
        modules = [ActivityModule::class])
interface ActivityComponent : AppProxyDependencies, ActivityProxyDependencies