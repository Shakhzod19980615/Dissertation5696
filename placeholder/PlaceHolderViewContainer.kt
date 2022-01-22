package ru.arzonpay.android.ui.placeholder

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.ui.util.ALPHA_OPAQUE
import ru.arzonpay.android.ui.util.ALPHA_TRANSPARENT
import ru.arzonpay.android.ui.util.changeValue
import ru.surfstudio.android.utilktx.util.SdkUtils
import java.util.concurrent.TimeUnit

private const val FADE_IN_DURATION = 150L
private const val STATE_TOGGLE_DELAY_MS = 250L

/**
 * Контейнер для смены вью, представляющих определенный LoadState
 */
class PlaceHolderViewContainer(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    val shimmerLayoutId: Int

    private var currentAnimation: ValueAnimator? = null
    private var loadStateSubject: BehaviorSubject<StatePresentation> = BehaviorSubject.create()

    private var stateDisposable = Disposables.disposed()
    private var hasFirstStateRendered = false

    init {
        layoutTransition = LayoutTransition()

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderViewContainer)
        shimmerLayoutId =
            ta.getResourceId(R.styleable.PlaceHolderViewContainer_shimmerLayout, View.NO_ID)
        ta.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        stateDisposable = loadStateSubject
            .debounce {
                when {
                    !hasFirstStateRendered -> {
                        hasFirstStateRendered = true
                        Observable.just(it)
                    }
                    else -> {
                        Observable.just(it)
                            .delay(STATE_TOGGLE_DELAY_MS, TimeUnit.MILLISECONDS)
                    }
                }
            }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { render(it) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (!stateDisposable.isDisposed) {
            stateDisposable.dispose()
        }
    }

    fun changeViewTo(view: View, shouldHide: Boolean = false) =
        loadStateSubject.onNext(StatePresentation(view, shouldHide))

    fun show() {
        currentAnimation?.cancel()
        visibility = View.VISIBLE
        currentAnimation = changeValue(
            alpha,
            ALPHA_OPAQUE,
            FADE_IN_DURATION,
            update = { this.alpha = it }
        )
    }

    fun hide() {
        currentAnimation?.cancel()
        currentAnimation = changeValue(
            alpha,
            ALPHA_TRANSPARENT,
            FADE_IN_DURATION,
            update = { this.alpha = it },
            complete = {
                visibility = View.GONE
                removeAllViews()
            }
        )
    }

    private fun render(statePresentation: StatePresentation) {
        val childCount = childCount
        when {
            statePresentation.shouldHide -> hide()
            else -> {
                show()
                addView(statePresentation.stateView)
                if (childCount > 0) removeViewAt(0)
            }
        }
    }

    /**
     * Этот метод нужен для того чтобы placeholder всегда отображался выше всех остальных элементов
     */
    @SuppressLint("NewApi")
    private fun moveOnTop() {
        SdkUtils.runOnLollipop {
            z = Float.MAX_VALUE
        }
    }

    /**
     * Сущность, представляющая состояние в виде вью или пустого объекта
     */
    data class StatePresentation(val stateView: View, val shouldHide: Boolean = false)
}

/**
 * Метод используется в тех случаях, когда PlaceHolderViewContainer должен перехватывать клики
 */
fun PlaceHolderViewContainer.setClickableAndFocusable(value: Boolean) {
    isClickable = value
    isFocusable = value
    isFocusableInTouchMode = value
}
