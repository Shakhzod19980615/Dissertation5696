package ru.arzonpay.android.ui.recylcer.decoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator

class SimpleOffsetDecorator(
    @Px val startOffset: Int = 0,
    @Px val topOffset: Int = 0,
    @Px val endOffset: Int = 0,
    @Px val bottomOffset: Int = 0
) : Decorator.OffsetDecor {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(startOffset, topOffset, endOffset, bottomOffset)
    }
}