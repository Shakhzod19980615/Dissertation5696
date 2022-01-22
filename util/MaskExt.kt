package ru.arzonpay.android.ui.util

import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy

@JvmInline
value class Mask(val format: String)

val CARD_MASK = Mask("[0000] [0000] [0000] [0000]")
val RU_PHONE_MASK = Mask("+7 ([000]) [000]-[00]-[00]")
val RU_LOCAL_MASK = Mask("8 ([000]) [000]-[00]-[00]")
val UZ_PHONE_MASK = Mask("+998 ([00]) [000]-[00]-[00]")

fun installMask(
    mask: Mask,
    editText: EditText,
    onValueChanged: (extractedValue: String, formattedValue: String) -> Unit
) {
    installOn(
        editText,
        mask.format,
        object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String
            ) {
                onValueChanged(extractedValue, formattedValue)
            }
        }
    )
}

fun installRuPhoneMask(
    editText: EditText,
    onValueChanged: (extractedValue: String, formattedValue: String) -> Unit
) {
    val affineFormats: MutableList<String> = ArrayList()
    affineFormats.add(RU_LOCAL_MASK.format)

    installOn(
        editText,
        RU_PHONE_MASK.format,
        affineFormats,
        AffinityCalculationStrategy.PREFIX,
        object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String
            ) {
                onValueChanged(extractedValue, formattedValue)
            }
        }
    )
}