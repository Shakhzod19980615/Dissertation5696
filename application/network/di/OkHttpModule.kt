package ru.arzonpay.android.application.network.di

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.arzonpay.android.f_debug.injector.DebugAppInjector
import ru.arzonpay.android.i_network.network.cache.SimpleCacheInterceptor
import ru.arzonpay.android.i_network.network.etag.EtagInterceptor
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.arzonpay.android.i_token.TokenStorage
import ru.surfstudio.android.dagger.scope.PerApplication
import java.util.concurrent.TimeUnit
import javax.inject.Named

const val DI_NAME_SERVICE_INTERCEPTOR = "DI_NAME_SERVICE_INTERCEPTOR"
private const val NETWORK_TIMEOUT = 30L //sec

/**
 * этот модуль вынесен отдельно для возможности замены его при интеграционном тестировании
 */
@Module
object OkHttpModule {

    @Provides
    @PerApplication
    @Named(DI_NAME_SERVICE_INTERCEPTOR)
    internal fun provideServiceInterceptor(
        tokenStorage: TokenStorage,
        settingsInteractor: SettingsInteractor
    ): Interceptor {
        return ServiceInterceptor(tokenStorage, settingsInteractor)
    }

    @Provides
    @PerApplication
    internal fun provideOkHttpClient(
        @Named(DI_NAME_SERVICE_INTERCEPTOR) serviceInterceptor: Interceptor,
        cacheInterceptor: SimpleCacheInterceptor,
        etagInterceptor: EtagInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)

            DebugAppInjector.debugInteractor.configureOkHttp(this)
            addInterceptor(cacheInterceptor)
            addInterceptor(etagInterceptor)
            addInterceptor(serviceInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }
}
