package ru.arzonpay.android.application.network.di

import android.app.DownloadManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.arzonpay.android.base_feature.BuildConfig
import ru.arzonpay.android.i_network.PROD_API_URL
import ru.arzonpay.android.i_network.network.BaseUrl
import ru.arzonpay.android.i_network.network.CallAdapterFactory
import ru.arzonpay.android.i_network.network.calladapter.BaseCallAdapterFactory
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.logger.Logger
import ru.arzonpay.android.f_debug.injector.DebugAppInjector
import ru.arzonpay.android.i_network.TEST_API_URL

@Module
object NetworkModule {

    private const val HTTP_LOG_TAG = "OkHttp"

    @Provides
    @PerApplication
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
        callAdapterFactory: BaseCallAdapterFactory,
        gson: Gson,
        apiUrl: BaseUrl
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(apiUrl.toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(callAdapterFactory)
            .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                ru.arzonpay.android.i_network.converter.gson.ResponseTypeAdapterFactory(
                    ru.arzonpay.android.i_network.converter.gson.SafeConverterFactory()
                )
            )
            .create()
    }

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) = Logger.d("$HTTP_LOG_TAG $message")
        }
        return HttpLoggingInterceptor(logger).apply {
            level = when (BuildConfig.DEBUG) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    @Provides
    @PerApplication
    internal fun provideDownloadManager(context: Context): DownloadManager {
        return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    @PerApplication
    internal fun provideCallAdapterFactory(): BaseCallAdapterFactory = CallAdapterFactory()

    @Provides
    @PerApplication
    internal fun provideBaseUrl(): BaseUrl {
        return if (DebugAppInjector.debugInteractor.isTestServerEnabled) {
            BaseUrl(TEST_API_URL, null)
        } else {
            BaseUrl(PROD_API_URL, null)
        }
    }
}