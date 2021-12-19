package ru.arzonpay.android.f_auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import ru.arzonpay.android.f_auth.AuthEvent.Input
import ru.arzonpay.android.f_auth.AuthEvent.KeyboardHeightChanged
import ru.arzonpay.android.f_auth.databinding.FragmentAuthBinding
import ru.arzonpay.android.f_auth.di.AuthScreenConfigurator
import ru.arzonpay.android.ui.mvi.view.BaseMviFragmentView
import ru.arzonpay.android.ui.placeholder.LoadStateView
import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.util.KeyboardVisibilityUtil
import ru.arzonpay.android.ui.util.back_press.hideKeyboard
import ru.arzonpay.android.ui.util.installRuPhoneMask
import ru.arzonpay.android.ui.view.extensions.distinctText
import ru.arzonpay.android.ui.view.extensions.setOnDoneAction
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.utilktx.ktx.ui.view.actionIfChanged
import javax.inject.Inject

internal class AuthFragmentView : BaseMviFragmentView<AuthState, AuthEvent>(),
    CrossFeatureFragment,
    LoadStateView {

    @Inject
    override lateinit var hub: ScreenEventHub<AuthEvent>

    @Inject
    override lateinit var sh: AuthStateHolder

    @Inject
    lateinit var ch: AuthCommandHolder

    private val binding by viewBinding(FragmentAuthBinding::bind)

    override var renderer: DefaultLoadStateRenderer? = null

    override fun createConfigurator() = AuthScreenConfigurator(arguments)

    override fun getScreenName() = "AuthFragmentView"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onResume() = with(binding) {
        super.onResume()
        KeyboardVisibilityUtil.keyboardVisibilityListener(
            isKeyboardVisibleAction = { height ->
                KeyboardHeightChanged(height, root.measuredHeight).emit()
            },
            isKeyboardGoneAction = { height ->
                root.postDelayed(
                    { KeyboardHeightChanged(height, root.measuredHeight).emit() },
                    100L
                )
            },
            decorView = root
        )
    }

    override fun onPause() {
        super.onPause()
        KeyboardVisibilityUtil.removeKeyboardVisibilityListener(binding.root)
    }

    override fun initViews() {
        renderer = DefaultLoadStateRenderer(binding.placeholderContainer)
        installRuPhoneMask(binding.phoneEt) { extractedValue, formattedValue ->
            Input.PhoneEdited(formattedValue, extractedValue).emit()
        }
        initListeners()
        ch.hideKeyboard bindTo { hideKeyboard() }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        renderer = null
        super.onDestroyView()
    }

    private fun initListeners() = with(binding) {
        authButton.setOnClickListener {
            Input.SubmitClicked.emit()
        }
        phoneEt.setOnDoneAction { Input.SubmitClicked.emit() }
        anonymousAuthBtn.setOnClickListener { Input.AnonymousAuthClicked.emit() }
    }

    override fun render(state: AuthState) {
        binding.phoneEt.distinctText = state.rawPhoneText
        binding.phoneTil.error = state.phoneErrorText
        renderLoadState(state.loadState)
        binding.authButton.actionIfChanged(state.isAuthBtnVisible) { _ ->
            binding.authBtnContainer.isVisible = state.isAuthBtnVisible
        }
    }
}