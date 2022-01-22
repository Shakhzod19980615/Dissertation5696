package ru.arzonpay.android.ui.recylcer.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.arzonpay.android.ui.recylcer.decoration.rule.Rules

/**
 * Базовый класс DividerDrawer'а для отрисовки однотонного разделителя элементов в вертикальном списке.
 */
abstract class BaseLinearDividerDrawer(
    gap: Gap
) : Decorator.ViewHolderDecor {

    protected val dividerDrawingRule = gap.rule

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gap.color
        strokeWidth = gap.height.toFloat()
    }
    private val alpha = dividerPaint.alpha
    private val paddingStart: Int = gap.paddingStart
    private val paddingEnd: Int = gap.paddingEnd

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val currentViewHolder = recyclerView.getChildViewHolder(view)
        val nextViewHolder = recyclerView.findViewHolderForAdapterPosition(currentViewHolder.adapterPosition + 1)

        val startX = recyclerView.paddingLeft + paddingStart
        val startY = view.bottom + view.translationY
        val stopX = recyclerView.width - recyclerView.paddingRight - paddingEnd
        val stopY = startY

        dividerPaint.alpha = (view.alpha * alpha).toInt()

        val isLastHolder = nextViewHolder == null
        val areSameHolders = currentViewHolder.itemViewType == nextViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER

        val shouldDrawMiddleDivider = shouldDrawMiddleDivider(areSameHolders)
        val shouldDrawEndDivider = shouldDrawEndDivider(areSameHolders, isLastHolder)

        val shouldDrawDivider = shouldDrawDivider(shouldDrawMiddleDivider, shouldDrawEndDivider, state)

        if (shouldDrawDivider) {
            canvas.drawLine(startX.toFloat(), startY, stopX.toFloat(), stopY, dividerPaint)
        }
    }

    protected open fun shouldDrawMiddleDivider(areSameHolders: Boolean): Boolean {
        return Rules.checkMiddleRule(dividerDrawingRule) && areSameHolders
    }

    protected open fun shouldDrawEndDivider(areSameHolders: Boolean, isLastHolder: Boolean): Boolean {
        return Rules.checkEndRule(dividerDrawingRule) && !areSameHolders
    }

    protected open fun shouldDrawDivider(
        shouldDrawMiddleDivider: Boolean,
        shouldDrawEndDivider: Boolean,
        state: RecyclerView.State
    ): Boolean {
        return shouldDrawMiddleDivider || shouldDrawEndDivider
    }
}
