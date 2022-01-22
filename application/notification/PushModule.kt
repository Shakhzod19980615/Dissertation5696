package ru.arzonpay.android.application.notification

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.arzonpay.android.i_push_notification.storage.DeviceApi
import ru.arzonpay.android.i_push_notification.storage.DeviceStorage
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@Module
object PushModule {

    @Provides
    @PerApplication
    fun provideFcmStorage(
        @Named(NO_BACKUP_SHARED_PREF) noBackupSharedPref: SharedPreferences
    ): DeviceStorage {
        return DeviceStorage(noBackupSharedPref)
    }

    @Provides
    @PerApplication
    fun provideDeviceApi(retrofit: Retrofit): DeviceApi = retrofit.create()
}