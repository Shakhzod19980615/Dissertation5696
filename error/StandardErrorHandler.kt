package ru.arzonpay.android.ui.error

import android.text.TextUtils
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.i_network.error.HttpProtocolException
import ru.arzonpay.android.i_network.error.NetworkErrorHandler
import ru.arzonpay.android.i_network.error.NonAuthorizedException
import ru.arzonpay.android.ui.navigation.routes.AuthFragmentRoute
import ru.surfstudio.android.navigation.command.fragment.RemoveAll
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import javax.inject.Inject

/**
 * Стандартный обработчик ошибок, возникающих при работе с сервером
 */
@PerScreen
open class StandardErrorHandler @Inject constructor(
    private val messageController: MessageController,
    private val appCommandExecutor: AppCommandExecutor
) : NetworkErrorHandler() {

    override fun handleHttpProtocolException(e: HttpProtocolException) {
        if (e is NonAuthorizedException) {
            appCommandExecutor.execute(listOf(RemoveAll(), Replace(AuthFragmentRoute())))
            return
        }

        if (e.httpCode >= ru.arzonpay.android.i_network.network.error.HttpCodes.CODE_500) {
            messageController.show(R.string.server_error_message)
        } else if (e.httpCode == ru.arzonpay.android.i_network.network.error.HttpCodes.CODE_403) {
            messageController.show(R.string.forbidden_error_error_message)
        } else if (!TextUtils.isEmpty(e.httpMessage)) {
            Logger.e(e.httpMessage)
        } else if (e.httpCode == ru.arzonpay.android.i_network.network.error.HttpCodes.CODE_404) {
            messageController.show(R.string.server_error_not_found)
        } else {
            messageController.show(R.string.default_http_error_message)
        }
    }

    override fun handleNoInternetError(e: ru.arzonpay.android.i_network.network.error.NoInternetException) {
        messageController.show(R.string.no_internet_connection_error_message)
    }

    override fun handleConversionError(e: ru.arzonpay.android.i_network.network.error.ConversionException) {
        messageController.show(R.string.bad_response_error_message)
    }

    override fun handleOtherError(e: Throwable) {
        messageController.show(R.string.unexpected_error_error_message)
        Logger.e(e, "Unexpected error")
    }
}