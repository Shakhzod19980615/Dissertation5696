package ru.arzonpay.android.application.network.di

import ru.arzonpay.android.i_token.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import ru.arzonpay.android.i_settings.SettingsInteractor
import ru.surfstudio.android.dagger.scope.PerApplication
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

const val HEADER_AUTH_KEY = "Authorization"
const val LANGUAGE_KEY = "Language"

/**
 * добавляет необходимые для каждого запроса параметры, такие как token
 */
@PerApplication
class ServiceInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val settingsInteractor: SettingsInteractor
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.headers[HEADER_AUTH_KEY] != null) {
            return chain.proceed(originalRequest)
        }

        val headersBuilder = originalRequest.headers.newBuilder()
            .add(LANGUAGE_KEY, settingsInteractor.getLanguage().code)
            .add(HEADER_AUTH_KEY, "Bearer ${tokenStorage.token}")

        val request = originalRequest.newBuilder()
            .headers(headersBuilder.build())
            .build()
        return chain.proceed(request)
    }
}
