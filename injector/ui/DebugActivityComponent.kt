package ru.arzonpay.android.f_debug.injector.ui

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.arzonpay.android.f_debug.DebugInteractor
import ru.arzonpay.android.f_debug.injector.DebugAppComponent
import ru.arzonpay.android.i_push_notification.storage.DeviceStorage
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

/**
 * Компонент для @PerActivity скоупа
 */
@PerActivity
@Component(dependencies = [DebugAppComponent::class],
        modules = [DebugActivityModule::class])
interface DebugActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun activityProvider(): ActivityProvider
    fun resourceProvider(): ResourceProvider
    fun debugInteractor(): DebugInteractor

    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun fragmentNavigator(): FragmentNavigator
    fun tabFragmentNavigator(): TabFragmentNavigator
    fun globalNavigator(): GlobalNavigator
    fun fcmStorage(): DeviceStorage

    @Named(NO_BACKUP_SHARED_PREF)
    fun sharedPreferences(): SharedPreferences
}