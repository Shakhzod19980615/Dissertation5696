package ru.arzonpay.android.f_details.decor

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import ru.arzonpay.android.f_details.data.NoBgField
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.surfstudio.android.easyadapter.item.BindableItem
import ru.surfstudio.android.recycler.decorator.easyadapter.BaseViewHolderDecor

internal class BackgroundOverlayDecor(
    @ColorInt private val fillColor: Int
) : BaseViewHolderDecor<BindableItem<*, *>> {

    private val path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        color = fillColor
    }
    private val rectF = RectF()
    private val roundTop = floatArrayOf(
        16f.toPx, 16f.toPx,
        16f.toPx, 16f.toPx,
        0f, 0f,
        0f, 0f
    )
    private val roundBottom = floatArrayOf(
        0f, 0f,
        0f, 0f,
        16f.toPx, 16f.toPx,
        16f.toPx, 16f.toPx
    )
    private val roundAll = floatArrayOf(
        16f.toPx, 16f.toPx,
        16f.toPx, 16f.toPx,
        16f.toPx, 16f.toPx,
        16f.toPx, 16f.toPx
    )
    private val roundNone = floatArrayOf(
        0f, 0f,
        0f, 0f,
        0f, 0f,
        0f, 0f
    )

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        baseItem: BindableItem<*, *>?
    ) {
        val prevItem = baseItem?.previousItem as? BindableItem<*, *>
        val nextItem = baseItem?.nextItem as? BindableItem<*, *>

        val shouldRoundTop = prevItem == null || prevItem.data is NoBgField
        val shouldRoundBottom = nextItem == null || nextItem.data is NoBgField
        val shouldRoundAll = shouldRoundBottom && shouldRoundTop

        val round = when {
            shouldRoundAll -> roundAll
            shouldRoundTop -> roundTop
            shouldRoundBottom -> roundBottom
            else -> roundNone
        }

        path.reset()
        rectF.apply {
            left = view.left.toFloat()
            top = view.top.toFloat()
            right = view.right.toFloat()
            bottom = view.bottom.toFloat()
        }
        path.addRoundRect(rectF, round, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }
}