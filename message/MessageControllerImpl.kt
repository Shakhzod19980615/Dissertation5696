package ru.arzonpay.android.ui.message

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.snackbar.Snackbar
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.message.*
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.view.extensions.getString
import ru.surfstudio.android.logger.Logger

/**
 * Базовый класс контроллера отображения сообщений
 * Для нахождения view использует fragment и затем activity провайдеры
 */
class MessageControllerImpl @JvmOverloads constructor(
    val activityProvider: ActivityProvider,
    val fragmentProvider: FragmentProvider? = null
) : MessageController {

    @ColorInt
    private var snackBarBackgroundColor: Int? = null

    @ColorInt
    private var snackBarActionButtonColor: Int? = null
    private var toast: Toast? = null
    private var snackbar: Snackbar? = null

    init {
        val typedArray = activityProvider.get()
            .obtainStyledAttributes(
                intArrayOf(
                    R.attr.snackBarBackgroundColor,
                    R.attr.snackBarActionButtonColor
                )
            )
        try {
            if (typedArray.hasValue(R.styleable.Core_snackBarBackgroundColor)) {
                snackBarBackgroundColor =
                    typedArray.getColor(R.styleable.Core_snackBarBackgroundColor, Color.TRANSPARENT)
            }
            if (typedArray.hasValue(R.styleable.Core_snackBarActionButtonColor)) {
                snackBarActionButtonColor = typedArray.getColor(
                    R.styleable.Core_snackBarActionButtonColor,
                    Color.TRANSPARENT
                )
            }
        } catch (ignored: UnsupportedOperationException) {
            // ignored
        } finally {
            typedArray.recycle()
        }
    }

    override fun show(
        message: CharSequence,
        @ColorRes
        backgroundColorResId: Int?,
        @StringRes
        actionResId: Int?,
        @ColorRes
        actionColorResId: Int?,
        duration: Int,
        dismissListener: (DismissReason) -> Unit,
        listener: (view: View) -> Unit
    ) {
        show(
            SnackParams(
                message = message,
                backgroundColorResId = backgroundColorResId ?: 0,
                actionResId = actionResId ?: 0,
                actionColorResId = actionColorResId ?: 0,
                duration = duration
            ),
            dismissListener,
            listener
        )
    }

    override fun show(
        @StringRes
        messageResId: Int,
        @ColorRes
        backgroundColorResId: Int?,
        @StringRes
        actionResId: Int?,
        @ColorRes
        actionColorResId: Int?,
        duration: Int,
        dismissListener: (DismissReason) -> Unit,
        listener: (view: View) -> Unit
    ) {
        show(
            SnackParams(
                messageResId = messageResId,
                backgroundColorResId = backgroundColorResId ?: 0,
                actionResId = actionResId ?: 0,
                actionColorResId = actionColorResId ?: 0,
                duration = duration
            ),
            dismissListener,
            listener
        )
    }

    override fun show(
        params: SnackParams,
        dismissListener: (DismissReason) -> Unit,
        actionListener: (view: View) -> Unit
    ) {
        val activity = activityProvider.get()
        val message = if (params.messageResId != 0) {
            activity.getString(params.messageResId)
        } else {
            params.message
        }
        var duration = params.duration
        val shouldUseDefaultDuration = duration != Snackbar.LENGTH_SHORT &&
                duration != Snackbar.LENGTH_LONG &&
                duration != Snackbar.LENGTH_INDEFINITE
        if (shouldUseDefaultDuration) {
            duration = Snackbar.LENGTH_SHORT
        }

        snackbar = Snackbar.make(getView(), message, duration).apply {
            var backgroundColor: Int? = if (params.backgroundColorResId != 0) {
                ContextCompat.getColor(activity, params.backgroundColorResId)
            } else {
                params.backgroundColor
            }
            if (backgroundColor == null) {
                backgroundColor = snackBarBackgroundColor
            }
            if (backgroundColor != null) {
                view.setBackgroundColor(backgroundColor)
            }
            val actionText = if (params.actionResId != 0) {
                activity.getString(params.actionResId)
            } else {
                params.action
            }
            if (actionText.isNotEmpty()) {
                setAction(actionText) { view -> actionListener.invoke(view) }
            }
            var actionButtonColor = if (params.actionColorResId != 0) {
                ContextCompat.getColor(activity, params.actionColorResId)
            } else {
                params.actionColor
            }
            if (actionButtonColor == null) {
                actionButtonColor = snackBarActionButtonColor
            }
            if (actionButtonColor != null) {
                setActionTextColor(actionButtonColor)
            }
            getSnackAnchorView(activityProvider, fragmentProvider)?.let { anchorView ->
                if (!anchorView.isGone) setAnchorView(anchorView)
            }

//            addCallback(SnackbarDismissCallback(dismissListener))
            show()
        }
    }

    override fun closeSnack() {
        snackbar?.dismiss()
    }

    override fun showToast(
        @StringRes messageResId: Int,
        gravity: Int?,
        duration: Int
    ) {
        showToast(
            ToastParams(
                messageResId = messageResId,
                gravity = gravity,
                duration = duration
            )
        )
    }

    override fun showToast(
        message: CharSequence,
        gravity: Int?,
        duration: Int
    ) {
        showToast(
            ToastParams(
                message = message,
                gravity = gravity,
                duration = duration
            )
        )
    }

    @SuppressLint("ShowToast")
    override fun showToast(params: ToastParams) {
        toast?.cancel()
        val activity = activityProvider.get()
        val toast: Toast
        var duration = params.duration
        if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG) {
            duration = Toast.LENGTH_SHORT
        }
        if (params.customView == null) {
            val message = if (params.messageResId != 0) {
                activity.getString(params.messageResId)
            } else {
                params.message
            }
            toast = Toast.makeText(activity, message, duration)
        } else {
            toast = Toast(activity)
            toast.view = params.customView
            toast.duration = params.duration
        }
        params.gravity?.let {
            toast.setGravity(it, params.xOffset, params.yOffset)
        }
        toast.show()
        this.toast = toast
    }

    /**
     * Порядок поиска подходящей корневой вью для SnackBar происходит со следующим приоритетом:
     * R.id.snackbar_container во фрагменте, должен быть FrameLayout
     * R.id.coordinator во фрагменте, должен быть CoordinatorLayout
     * R.id.snackbar_container в активити, должен быть CoordinatorLayout или FrameLayout
     * R.id.coordinator в активити, должен быть CoordinatorLayout
     * android.R.id.content активити
     *
     *
     * Для того, чтобы срабатывал Behavior на появление Snackbar,
     * нужно чтобы найденая View была [CoordinatorLayout]
     */
    private fun getView(): View {
        return getViewFromFragment(fragmentProvider)
            ?: getViewFromActivity(activityProvider)
            ?: throw ViewForSnackbarNotFoundException("ViewForSnackbarNotFound")
    }

    private fun getViewFromFragment(fragmentProvider: FragmentProvider?): View? {
        val fragmentView = fragmentProvider?.get()?.view ?: return null
        return fragmentView.findViewById(R.id.snackbar_container)
            ?: fragmentView.findViewById(R.id.coordinator)
    }

    private fun getViewFromActivity(activityProvider: ActivityProvider): View? {
        val activity = activityProvider.get()
        return activity.findViewById(R.id.snackbar_container)
            ?: activity.findViewById(R.id.coordinator)
            ?: activity.findViewById(android.R.id.content)
    }

    /**
     * R.id.snack_anchor - помечается View/ViewGroup над которым необходимо показать снекбар
     */
    private fun getSnackAnchorView(
        activityProvider: ActivityProvider,
        fragmentProvider: FragmentProvider?
    ): View? {
        return getSnackAnchorView(fragmentProvider) ?: getSnackAnchorView(activityProvider)
    }

    private fun getSnackAnchorView(fragmentProvider: FragmentProvider?): View? {
        val fragmentView = fragmentProvider?.get()?.view ?: return null
        return fragmentView.findViewWithTag(fragmentView.getString(R.string.snackbar_anchor_tag))
    }

    private fun getSnackAnchorView(activityProvider: ActivityProvider): View? {
        val activity = activityProvider.get()
        val decorView = activity.window?.decorView as? ViewGroup
        return decorView?.findViewWithTag(activity.getString(R.string.snackbar_anchor_tag))
    }
}