package ru.arzonpay.android.application.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider

@Module
object EtagModule {

    @Provides
    @PerApplication
    internal fun provideEtagCache(context: Context): ru.arzonpay.android.i_network.network.etag.storage.EtagCache {
        return ru.arzonpay.android.i_network.network.etag.storage.EtagCache(
            AppDirectoriesProvider.provideNoBackupStorageDir(
                context
            )
        )
    }

    @Provides
    @PerApplication
    internal fun provideEtagStorage(etagCache: ru.arzonpay.android.i_network.network.etag.storage.EtagCache): ru.arzonpay.android.i_network.network.etag.storage.EtagStorage {
        return ru.arzonpay.android.i_network.network.etag.storage.EtagStorage(etagCache)
    }

    @Provides
    @PerApplication
    internal fun provideEtagInterceptor(etagStorage: ru.arzonpay.android.i_network.network.etag.storage.EtagStorage): ru.arzonpay.android.i_network.network.etag.EtagInterceptor {
        return ru.arzonpay.android.i_network.network.etag.EtagInterceptor(etagStorage)
    }
}