package ru.arzonpay.android.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.arzonpay.android.ui.placeholder.PlaceHolderViewContainer
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.placeholder.loadstate.state.ErrorLoadState
import ru.arzonpay.android.ui.placeholder.setClickableAndFocusable

/**
 * Представление состояния ErrorLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class ErrorLoadStatePresentation(
    private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<ErrorLoadState>() {

    @StringRes
    var messageTextRes: Int = R.string.common_error_message

    private lateinit var messageView: TextView
    private lateinit var reloadButton: Button

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context)
            .inflate(R.layout.layout_state_error, placeHolder, false)
            .apply {
                messageView = findViewById(R.id.error_load_state_tv)
                reloadButton = findViewById(R.id.error_load_state_btn)
            }
    }

    override fun showState(state: ErrorLoadState) {
        initViews(view)

        with(placeHolder) {
            changeViewTo(view)
            reloadButton.setOnClickListener { state.action() }
            setClickableAndFocusable(true)
            show()
        }
    }

    private fun initViews(view: View) {
        messageView.text = view.context.getString(messageTextRes)
    }
}