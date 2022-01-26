package ru.arzonpay.android.f_debug.common_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.LayoutTitleSubtitleSwitchBinding

/**
 * Виджет, позволяющий задать как title, так и subtitle у Switch
 */
class DebugTitleSubtitleSwitch(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val vb by lazy(LazyThreadSafetyMode.NONE) { LayoutTitleSubtitleSwitchBinding.bind(this) }

    private val onCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
        }

    private var isChecked: Boolean = false

    var title: String = ""
        set(value) {
            vb.debugTitleTv.text = value
        }

    var subtitle: String = ""
        set(value) {
            vb.debugSubtitleTv.text = value
        }


    init {
        View.inflate(context, R.layout.layout_title_subtitle_switch, this)

        initListeners()

        obtainAttributes(context, attrs)
    }

    fun isChecked() = isChecked

    fun setChecked(value: Boolean) {
        vb.debugValueSwitch.isChecked = value
    }

    private fun initListeners() {
        vb.debugValueSwitch.setOnCheckedChangeListener(onCheckedChangeListener)
        this.setOnClickListener { setChecked(!vb.debugValueSwitch.isChecked) }
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.DebugTitleSubtitleSwitch)
        vb.debugValueSwitch.isChecked = typedArray.getBoolean(
            R.styleable.DebugTitleSubtitleSwitch_debug_switch_checked,
            isChecked
        )
        title = typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_title) ?: ""
        subtitle =
            typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_subtitle) ?: ""
        typedArray.recycle()
    }

    fun setOnCheckedChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        vb.debugValueSwitch.setOnClickListener {
            onCheckedChangeListener.onCheckedChanged(
                vb.debugValueSwitch,
                vb.debugValueSwitch.isChecked
            )
            listener(vb.debugValueSwitch, isChecked)
        }
    }
}