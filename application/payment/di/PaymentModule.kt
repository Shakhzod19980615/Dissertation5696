package ru.arzonpay.android.application.payment.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.arzonpay.android.i_payment.PaymentApi
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
object PaymentModule {

    @Provides
    @PerApplication
    fun providePaymentApi(retrofit: Retrofit): PaymentApi {
        return retrofit.create()
    }
}