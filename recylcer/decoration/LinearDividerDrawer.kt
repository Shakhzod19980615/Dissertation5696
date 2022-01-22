package ru.arzonpay.android.ui.recylcer.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.arzonpay.android.ui.recylcer.decoration.rule.Rules
import ru.surfstudio.android.recycler.decorator.Decorator

class LinearDividerDrawer(private val gap: Gap) : Decorator.ViewHolderDecor {

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val alpha = dividerPaint.alpha

    init {
        dividerPaint.color = gap.color
        dividerPaint.strokeWidth = gap.height.toFloat()
    }

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = recyclerView.getChildViewHolder(view)
        val adapterPosition = viewHolder.adapterPosition
        val viewType = viewHolder.itemViewType

        val lastPosition = (recyclerView.adapter?.itemCount ?: 0) - 1

        val prevViewHolder = recyclerView.findViewHolderForAdapterPosition(adapterPosition - 1)
        val nextViewHolder = recyclerView.findViewHolderForAdapterPosition(adapterPosition + 1)

        val areDifferentFromNext = viewHolder.adapterPosition == lastPosition ||
            nextViewHolder != null && viewType != nextViewHolder.itemViewType

        val areSameWithNext = !areDifferentFromNext
        val areDifferentFromPrev = viewHolder.adapterPosition == 0 ||
            prevViewHolder != null && viewType != prevViewHolder.itemViewType

        val drawStartDivider = Rules.checkStartRule(gap.rule) && areDifferentFromPrev
        val drawMiddleDivider = Rules.checkMiddleRule(gap.rule) && areSameWithNext
        val drawEndDivider = Rules.checkEndRule(gap.rule) && areDifferentFromNext

        dividerPaint.alpha = (view.alpha * alpha).toInt()

        val startX = recyclerView.paddingLeft + gap.paddingStart
        val stopX = recyclerView.width - recyclerView.paddingRight - gap.paddingEnd

        if (drawStartDivider) {
            val startY = view.top + view.translationY + gap.height.toFloat() + gap.paddingTop
            val stopY = startY
            canvas.drawLine(startX.toFloat(), startY, stopX.toFloat(), stopY, dividerPaint)
        }

        if (drawMiddleDivider || drawEndDivider) {
            val startY = view.bottom + view.translationY - gap.height.toFloat() + gap.paddingTop
            val stopY = startY
            canvas.drawLine(startX.toFloat(), startY, stopX.toFloat(), stopY, dividerPaint)
        }
    }
}
