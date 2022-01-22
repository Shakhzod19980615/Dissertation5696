package ru.arzonpay.android.application.auth.di

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.arzonpay.android.i_auth.AnonymousAuthService
import ru.arzonpay.android.i_auth.AuthApi
import ru.arzonpay.android.i_auth.PhoneAuthService
import ru.arzonpay.android.i_auth.firebase.FirebaseAnonymousAuthService
import ru.arzonpay.android.i_auth.firebase.FirebasePhoneAuthService
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
object AuthModule {

    @Provides
    @PerApplication
    internal fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @PerApplication
    internal fun providePhoneService(activeActivityHolder: ActiveActivityHolder): PhoneAuthService {
        return FirebasePhoneAuthService(activeActivityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSmsRetriever(context: Context): SmsRetrieverClient {
        return SmsRetriever.getClient(context)
    }

    @Provides
    @PerApplication
    internal fun provideAnonymousAuthService(): AnonymousAuthService {
        return FirebaseAnonymousAuthService()
    }
}