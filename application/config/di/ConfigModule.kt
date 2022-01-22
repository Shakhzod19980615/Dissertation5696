package ru.arzonpay.android.application.config.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.arzonpay.android.i_config.ConfigApi
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
object ConfigModule {

    @Provides
    @PerApplication
    fun provideConfigApi(retrofit: Retrofit): ConfigApi {
        return retrofit.create()
    }
}