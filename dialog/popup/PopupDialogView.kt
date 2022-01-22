package ru.arzonpay.android.ui.dialog.popup

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.base_feature.databinding.DialogPopupBinding
import ru.arzonpay.android.ui.dialog.base.result.BaseResultBottomDialogView
import ru.arzonpay.android.ui.dialog.base.simple.SimpleResult
import ru.arzonpay.android.ui.util.EMPTY_RESOURCE
import ru.arzonpay.android.ui.view.extensions.expand
import ru.arzonpay.android.ui.view.extensions.getBottomSheetBehavior
import ru.arzonpay.android.ui.view.extensions.unsafeLazy
import ru.surfstudio.android.core.ui.view_binding.viewBinding

/**
 * Стандартный диалог
 *
 * Отображается после успешного выполнения какого-либо действия.
 *
 * По умолчанию, используется как диалог подтверждения
 * Для использования как модальный, нужно передать параметры primaryButton
 */
class PopupDialogView : BaseResultBottomDialogView<SimpleResult>() {

    override val route by unsafeLazy { PopupDialogRoute(requireArguments()) }

    override var result: SimpleResult? = SimpleResult.DISMISS

    private val binding by viewBinding(DialogPopupBinding::bind)

    override fun getTheme() = R.style.PopupDialogStyle

    override fun getName() = "PopupDialogView"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            getBottomSheetBehavior()?.apply {
                expand()
                skipCollapsed = true
                isCancelable = false
                isDraggable = false
                isHideable = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        isCancelable = route.isCancellable

        val isTitleVisible = route.titleText.isNotBlank()
        val isSubtitleVisible = route.subtitleText.isNotBlank()
        val isPrimaryButtonVisible = route.primaryButtonText.isNotBlank()
        val isSecondaryButtonVisible = route.secondaryButtonText.isNotBlank()

        if (route.iconRes != EMPTY_RESOURCE) {
            iconIv.setImageResource(route.iconRes)
        }

        if (isPrimaryButtonVisible) {
            primaryBtn.setOnClickListener {
                closeWithResult(SimpleResult.POSITIVE)
            }
        }

        if (isSecondaryButtonVisible) {
            secondaryBtn.setOnClickListener {
                closeWithResult(SimpleResult.NEGATIVE)
            }
        }

        titleTv.isVisible = isTitleVisible
        subtitleTv.isVisible = isSubtitleVisible
        primaryBtn.isVisible = isPrimaryButtonVisible
        secondaryBtn.isVisible = isSecondaryButtonVisible

        titleTv.text = route.titleText
        subtitleTv.text = route.subtitleText
        primaryBtn.text = route.primaryButtonText
        secondaryBtn.text = route.secondaryButtonText

        subtitleTv.movementMethod = LinkMovementMethod.getInstance()

        contentContainer.post { ensureContentHeight() }
    }

    private fun ensureContentHeight() = with(binding) {
        val contentContainerHeight = contentContainer.measuredHeight
        val rootHeight = rootContainer.measuredHeight

        val buttonsHeight = buttonsSpace.measuredHeight +
                primaryBtn.measuredHeight +
                secondaryBtn.measuredHeight

        val maxContentHeight = rootHeight - buttonsHeight

        if (contentContainerHeight > maxContentHeight) {
            contentContainer.updateLayoutParams { height = maxContentHeight }
        }
    }
}
