package ru.arzonpay.android.f_debug.debug.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import ru.arzonpay.android.f_debug.R
import ru.arzonpay.android.f_debug.databinding.DebugCatalogItemLayoutBinding

/**
 * Виджет для элемента каталога debug-screen
 */
class DebugCatalogItemLayout(
    context: Context,
    attributeSet: AttributeSet
) : LinearLayout(context, attributeSet) {

    private val vb by lazy(LazyThreadSafetyMode.NONE) { DebugCatalogItemLayoutBinding.bind(this) }

    private lateinit var debugItemName: String
    private lateinit var debugItemDescription: String
    private var debugItemIcon: Drawable? = null

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.debug_catalog_item_layout, this)
        obtainAttributes(context, attributeSet)
        updateView()
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.DebugCatalogItemLayout)
        debugItemName =
            typedArray.getString(R.styleable.DebugCatalogItemLayout_debug_catalog_item_name) ?: ""
        debugItemDescription =
            typedArray.getString(R.styleable.DebugCatalogItemLayout_debug_catalog_item_description)
                ?: ""
        debugItemIcon =
            typedArray.getDrawable(R.styleable.DebugCatalogItemLayout_debug_catalog_item_icon)
        typedArray.recycle()
    }

    private fun updateView() = with(vb) {
        debugItemNameTv.text = debugItemName
        debugItemDescriptionTv.text = debugItemDescription
        debugItemDescriptionTv.isVisible = debugItemDescription.isNotEmpty()
        if (debugItemIcon != null) {
            debugItemNameTv.setCompoundDrawablesWithIntrinsicBounds(
                debugItemIcon,
                null,
                null,
                null
            )
        }
    }
}