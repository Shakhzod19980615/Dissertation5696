package ru.arzonpay.android.ui.view.input

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.google.android.material.card.MaterialCardView
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.base_feature.databinding.ViewAmountInputBinding
import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.ui.util.getIcon
import ru.arzonpay.android.ui.view.extensions.errorText
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.arzonpay.android.ui.view.extensions.unsafeLazy
import java.math.BigDecimal

class AmountInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : MaterialCardView(context, attrs) {

    private val binding by unsafeLazy { ViewAmountInputBinding.bind(this) }

    var currency: Currency = Currency.SUM
        set(value) {
            if (field == value) return
            field = value
            binding.endActionIb.setImageResource(currency.getIcon())
            onCurrencyChangedListener(value)
        }

    var amount: BigDecimal?
        get() = binding.amountEt.amount
        set(value) {
            binding.amountEt.amount = value
        }

    var helperText: CharSequence?
        get() = binding.amountTil.helperText
        set(value) {
            binding.amountTil.helperText = value
        }

    var errorText: CharSequence?
        get() = binding.amountTil.error
        set(value) {
            binding.amountTil.errorText = value
        }

    var onCurrencyChangedListener: (Currency) -> Unit = {}

    var onAmountChanged: (BigDecimal?) -> Unit = {}

    init {
        cardElevation = 0f
        inflate(context, R.layout.view_amount_input, this)
        initAttrs(attrs)
        binding.endActionIb.setOnClickListener { toggleCurrency() }
        binding.amountEt.onAmountChangedAction = { _, new ->
            onAmountChanged(new)
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        context.withStyledAttributes(attrs, R.styleable.AmountInputLayout) {
            val shapeType = getInteger(R.styleable.AmountInputLayout_shapeType, BOTTOM_ROUND)
            initShape(shapeType)
        }
    }

    private fun toggleCurrency() {
        currency = if (currency == Currency.RUB) {
            Currency.SUM
        } else {
            Currency.RUB
        }
    }

    private fun initShape(shapeType: Int) {
        val radius = 16.toPx * 1f
        val notRound = 0f
        when (shapeType) {
            ROUND ->
                binding.amountTil.setBoxCornerRadii(radius, radius, radius, radius)
            BOTTOM_ROUND ->
                binding.amountTil.setBoxCornerRadii(notRound, notRound, radius, radius)
            TOP_ROUND ->
                binding.amountTil.setBoxCornerRadii(radius, radius, notRound, notRound)
        }
    }

    private companion object {
        const val ROUND = 1
        const val BOTTOM_ROUND = 2
        const val TOP_ROUND = 3
    }
}