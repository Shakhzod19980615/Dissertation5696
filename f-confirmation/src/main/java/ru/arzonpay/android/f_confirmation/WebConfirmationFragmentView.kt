package ru.arzonpay.android.f_confirmation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import ru.arzonpay.android.f_confirmation.WebConfirmationEvent.Input
import ru.arzonpay.android.f_confirmation.databinding.FragmentWebConfirmationBinding
import ru.arzonpay.android.f_confirmation.di.WebConfirmationScreenConfigurator
import ru.arzonpay.android.ui.mvi.view.BaseMviFragmentView
import ru.arzonpay.android.ui.placeholder.LoadStateView
import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.util.back_press.addDefaultOnBackPressedCallback
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.utilktx.ktx.ui.view.actionIfChanged
import javax.inject.Inject

internal class WebConfirmationFragmentView :
    BaseMviFragmentView<WebConfirmationState, WebConfirmationEvent>(),
    CrossFeatureFragment,
    LoadStateView {

    @Inject
    override lateinit var hub: ScreenEventHub<WebConfirmationEvent>

    @Inject
    override lateinit var sh: WebConfirmationStateHolder

    override var renderer: DefaultLoadStateRenderer? = null

    @Inject
    lateinit var ch: WebConfirmationCommandHolder

    private val binding by viewBinding(FragmentWebConfirmationBinding::bind)

    override fun createConfigurator() = WebConfirmationScreenConfigurator(requireArguments())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_confirmation, container, false)
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        renderer = null
        super.onDestroyView()
    }

    override fun initViews() = with(binding) {
        renderer = DefaultLoadStateRenderer(placeholderContainer)
        with(processWebView) {
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Input.LoadFinished.emit()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    error?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Logger.e("errorCode: ${it.errorCode}  description: ${it.description} url: ${request?.url}")
                        }
                    }
                    Input.LoadError.emit()
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Logger.e("errorCode: $errorCode  description: $description url: $failingUrl")
                    }
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url?.toString() ?: EMPTY_STRING
                    Input.Redirect(url).emit()
                    view.loadUrl(url, extraHeaders)
                    return true
                }
            }
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }

        toolbar.setNavigationOnClickListener { Input.CloseClicked.emit() }

        ch.reloadUrl bindTo {
            Input.LoadStarted.emit()
            binding.processWebView.reload()
        }

        addDefaultOnBackPressedCallback {
            if (binding.processWebView.canGoBack()) {
                binding.processWebView.goBack()
            } else {
                Input.CloseClicked.emit()
            }
        }
    }

    override fun render(state: WebConfirmationState) {
        binding.processWebView.actionIfChanged(state.url) { _ ->
            binding.processWebView.loadUrl(state.url, extraHeaders)
        }
        binding.toolbar.title = state.title
        renderLoadState(state.loadState)
    }

    private companion object {
        val extraHeaders: Map<String, String> get() =
            mutableMapOf("Referer" to "https://arzonpay.ru")
    }
}