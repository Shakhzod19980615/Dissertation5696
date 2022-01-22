package ru.arzonpay.android.application.cache.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.arzonpay.android.i_network.cache.SimpleCacheInfoStorage
import ru.arzonpay.android.i_network.network.BaseUrl
import ru.arzonpay.android.i_network.network.cache.SimpleCacheFactory
import ru.arzonpay.android.i_network.network.cache.SimpleCacheInterceptor
import ru.arzonpay.android.i_network.network.cache.SimpleCacheUrlConnector

/**
 * Dagger-модуль для удовлетворения зависимостей классов, использующихся для кэширования
 */
@Module
object CacheModule {

    @Provides
    @PerApplication
    internal fun provideSimpleCacheInterceptor(
        simpleCacheFactory: SimpleCacheFactory,
        simpleCacheUrlConnector: SimpleCacheUrlConnector
    ): SimpleCacheInterceptor {
        return SimpleCacheInterceptor(
            simpleCacheFactory,
            simpleCacheUrlConnector
        )
    }

    @Provides
    @PerApplication
    internal fun provideSimpleCacheFactory(
        context: Context,
        cacheUrlConnector: SimpleCacheUrlConnector
    ): SimpleCacheFactory {
        return SimpleCacheFactory(
            AppDirectoriesProvider.provideBackupStorageDir(context),
            cacheUrlConnector
        )
    }

    @Provides
    @PerApplication
    internal fun providesSimpleCacheConnector(
        baseUrl: BaseUrl,
        simpleCacheInfoStorage: SimpleCacheInfoStorage
    ): SimpleCacheUrlConnector {
        return SimpleCacheUrlConnector(
            baseUrl,
            simpleCacheInfoStorage.simpleCaches
        )
    }
}