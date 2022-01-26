package ru.arzonpay.android.f_debug.scalpel

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.ScalpelWidgetLayoutBinding
import java.util.concurrent.TimeUnit

/**
 * Виджет, сожержащий [DebugScalpelFrameLayout] и контролы для его настройки
 */
class DebugScalpelWidget(context: Context) : RelativeLayout(context) {

    private val vb by lazy(LazyThreadSafetyMode.NONE) { ScalpelWidgetLayoutBinding.bind(this) }

    private val scalpelSettings = DebugScalpelSettings(context)
    private val scalpelManager = DebugScalpelManager

    private val disposable = CompositeDisposable()

    init {
        inflate()
        initSettingsControls()
        initScalpel()
        initPanel()
    }

    private fun initScalpel() = with(vb.debugScalpel) {
        setDrawIds(scalpelSettings.drawIds)
        setDrawViewClasses(scalpelSettings.drawClasses)
        setDrawViews(scalpelSettings.drawViewsContent)
        isLayerInteractionEnabled = true
    }

    private fun initSettingsControls() = with(vb) {
        //Switchers
        debugDrawIdScalpelSettings.setChecked(scalpelSettings.drawIds)
        debugDrawClassScalpelSettings.setChecked(scalpelSettings.drawClasses)
        debugDrawViewsScalpelSettings.setChecked(scalpelSettings.drawViewsContent)

        debugDrawIdScalpelSettings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawIds = enabled
            debugScalpel.setDrawIds(enabled)
        }
        debugDrawClassScalpelSettings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawClasses = enabled
            debugScalpel.setDrawViewClasses(enabled)
        }
        debugDrawViewsScalpelSettings.setOnCheckedChangeListener { _, enabled ->
            scalpelSettings.drawViewsContent = enabled
            debugScalpel.setDrawViews(enabled)
        }

        //Range Bar
        debugViewsLayersScalpelSettings.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            debugScalpel.currentStartViewLayer = leftPinIndex
            debugScalpel.currentEndViewLayer = rightPinIndex
        }

        disposable.add(Observable.interval(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                debugScalpel.let { scalpel ->
                    with(debugViewsLayersScalpelSettings) {
                        val lastTickEnd = tickEnd.toInt()
                        if (lastTickEnd != scalpel.endViewLayer) {
                            tickEnd = scalpel.endViewLayer.toFloat()
                            if (rightIndex == lastTickEnd) {
                                setRangePinsByIndices(
                                    scalpel.currentStartViewLayer,
                                    scalpel.endViewLayer
                                )
                            }
                        }
                    }
                }

            })

        //Hide Btn
        debugHideScalpelSettingsBtn.setOnClickListener {
            toggleSettingsVisibility()
        }
    }

    private fun initPanel() = with(vb) {
        debugScalpelSettingsBtn.setOnClickListener {
            toggleSettingsVisibility()
        }
        debugCloseScalpelBtn.setOnClickListener {
            scalpelManager.disableScalpel()
        }
    }

    private fun toggleSettingsVisibility() {
        vb.debugScalpelSettingsBtn.isVisible = isVisible.not()
    }

    private fun inflate() {
        View.inflate(context, R.layout.scalpel_widget_layout, this)
    }

    fun addContentViews(childViews: List<View>) {
        childViews.forEach { vb.debugScalpel.addView(it) }
    }

    fun extractContentViews(): List<View> {
        val childViews = (0 until vb.debugScalpel.childCount)
            .map { vb.debugScalpel.getChildAt(it) }
            .toList()
        vb.debugScalpel.removeAllViews()
        return childViews
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.clear()
    }
}