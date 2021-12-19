package ru.arzonpay.android.f_auth.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import ru.arzonpay.android.f_auth.R
import ru.arzonpay.android.f_auth.code.di.SmsCodeScreenConfigurator
import ru.arzonpay.android.f_auth.code.view.fitParent
import ru.arzonpay.android.f_auth.databinding.FragmentSmsCodeBinding
import ru.arzonpay.android.ui.mvi.view.BaseMviFragmentView
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.arzonpay.android.f_auth.code.SmsCodeEvent.*
import ru.arzonpay.android.f_auth.code.view.PinEntryView
import ru.arzonpay.android.ui.placeholder.LoadStateView
import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.view.extensions.*
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.utilktx.ktx.ui.view.showKeyboard
import javax.inject.Inject

internal class SmsCodeFragmentView : BaseMviFragmentView<SmsCodeState, SmsCodeEvent>(),
    CrossFeatureFragment,
    LoadStateView {

    @Inject
    override lateinit var hub: ScreenEventHub<SmsCodeEvent>

    @Inject
    override lateinit var sh: SmsCodeStateHolder

    @Inject
    lateinit var resourceProvider: ResourceProvider

    private val binding by viewBinding(FragmentSmsCodeBinding::bind)
    override var renderer: DefaultLoadStateRenderer? = null

    override fun createConfigurator() = SmsCodeScreenConfigurator(arguments)

    override fun getScreenName() = "SmsCodeFragmentView"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sms_code, container, false)
    }

    override fun onDestroyView() {
        renderer = null
        super.onDestroyView()
    }

    override fun initViews() = with(binding) {
        renderer = DefaultLoadStateRenderer(otpPlaceholderContainer)
        otpPev.fitParent(
            otpPev.displayMetrics.widthPixels,
            24.toPx
        )
        otpPev.editText.requestFocus()
        otpPev.editText.showKeyboard()

        toolbar.setNavigationOnClickListener { Input.CloseClicked.emit() }
        resendBtn.setOnClickListener { Input.ResendCode.emit() }
        otpPev.onPinEnteredListener =
            PinEntryView.OnPinEnteredListener { Input.CodeChanged(it).emit() }
    }

    override fun render(state: SmsCodeState) = with(binding) {
        renderLoadState(state.loadState)
        otpMessageTv.distinctText = state.message
        otpResendTv.distinctText = state.timeText
        otpResendTv.isVisible = !state.isResendAllowed
        otpPev.setErrorState(state.hasError)
        otpPev.performIfChanged<String>("${state.inputtedCode}${state.loadState}") {
            val isEnabled = !state.loadState.isLoading
            otpPev.editText.distinctText = state.inputtedCode
            otpPev.editText.isEnabled = isEnabled
            otpPev.isEnabled = isEnabled
        }
        resendBtn.isVisible = state.isResendAllowed
    }
}