package ru.arzonpay.android.f_form

import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.domain.payment.FieldType
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.domain.payment.FormItem
import ru.arzonpay.android.f_form.FormEvent.*
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.ui.mvi.mapper.RequestMappers
import ru.arzonpay.android.ui.util.*
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Reducer [FormFragmentView]
 */
@PerScreen
internal class FormReducer @Inject constructor(
    dependency: BaseReactorDependency,
    private val ch: FormCommandHolder,
    private val resourceProvider: ResourceProvider
) : BaseReducer<FormEvent, FormState>(dependency) {

    override fun reduce(state: FormState, event: FormEvent): FormState {
        return when (event) {
            is ConfigLoaded -> onConfigLoaded(state, event)
            is FormRequest -> onFormRequest(state, event)
            is Fields -> onFieldEdited(state, event)
            is Input.PayClicked -> validateFields(state)
            is SendFormRequest -> onSendForm(state, event)
            is GetFieldValues -> onGetFieldValues(state, event)
            is FieldValueSelected -> onFieldValueSelected(state, event)
            is Input.SelectorClicked -> onSelectorClicked(state, event)
            is Input.BackClicked -> onBackClicked(state)
            else -> state
        }
    }

    private fun onFormRequest(state: FormState, event: FormRequest): FormState {
        val newRequestUi = RequestMapper.builder(event.request)
            .mapLoading(RequestMappers.loading.mainOrNone())
            .handleError(RequestMappers.error.loadingBased(errorHandler))
            .mapData(RequestMappers.data.single())
            .build()

        return state.copy(
            loadState = newRequestUi.loadStateType,
            fields = newRequestUi.data ?: emptyList(),
            uiFields = initUiFields(newRequestUi, state.formItems)
        )
    }

    private fun onSendForm(state: FormState, event: SendFormRequest): FormState {
        val newRequestUi = RequestMapper.builder(event.request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError(RequestMappers.error.forced(errorHandler))
            .mapData(RequestMappers.data.single())
            .build()

        return state.copy(loadState = newRequestUi.loadStateType)
    }

    private fun onGetFieldValues(state: FormState, event: GetFieldValues): FormState {
        val newRequestUi = RequestMapper.builder(event.request)
            .mapLoading(RequestMappers.loading.transparentOrNone())
            .handleError(RequestMappers.error.forced(errorHandler))
            .mapData(RequestMappers.data.single())
            .build()

        return state.copy(loadState = newRequestUi.loadStateType)
    }

    private fun onConfigLoaded(state: FormState, event: ConfigLoaded): FormState {
        return state.copy(
            rate = event.rate,
            minUzs = event.minUzs,
            maxUzs = event.maxUzs
        )
    }

    private fun onBackClicked(state: FormState): FormState {
        ch.hideKeyboard.accept()
        return state
    }

    private fun onFieldValueSelected(state: FormState, event: FieldValueSelected): FormState {
        val selectedField = state.uiFields.filterIsInstance<FieldUi.Selector>()
            .firstOrNull { it.id == event.fieldId }

        fun changeSelectorInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Selector && fieldUi.id == event.fieldId) {
                return fieldUi.copy(value = event.value, errorText = null)
            }
            if (fieldUi is FieldUi.Selector && fieldUi.parent == selectedField?.name) {
                return fieldUi.copy(value = null)
            }
            return fieldUi
        }
        return state.copy(uiFields = state.uiFields.map(::changeSelectorInfo))
    }

    private fun onSelectorClicked(state: FormState, event: Input.SelectorClicked): FormState {
        fun changeSelectorInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Selector && fieldUi.name == event.field.parent) {
                return fieldUi.copy(errorText = resourceProvider.getString(R.string.feed_selector_parent_not_selected))
            }
            return fieldUi
        }
        if (event.field.parent != null && state.getParent(event.field.parent) == null) {
            return state.copy(uiFields = state.uiFields.map(::changeSelectorInfo))
        }
        return state
    }

    private fun initUiFields(
        request: RequestUi<List<Field>>,
        formItems: List<FormItem>
    ): List<FieldUi> {
        val fields = request.data ?: return emptyList()
        val result = mutableListOf<FieldUi>()
        for (field in fields) {
            val value = formItems.firstOrNull { it.name == field.name }?.value ?: EMPTY_STRING
            val fieldUi = when (field.type) {
                FieldType.CARD -> field.toCardUi(value)
                FieldType.DIGITS -> field.toDigitUi(value)
                FieldType.MONEY -> field.toMoneyUi()
                FieldType.MULTILINE -> field.toMultilineUi(value)
                FieldType.PHONE -> field.toPhoneUi(value)
                FieldType.SELECTOR -> field.toSelectorUi(value)
                FieldType.TEXT -> field.toTextUi(value)
            }
            result.add(fieldUi)
        }
        return result
    }

    private fun validateFields(state: FormState): FormState {
        fun getFieldByName(name: String): Field? {
            return state.fields.firstOrNull { it.name == name }
        }

        fun validateCardField(fieldUi: FieldUi.Card): FieldUi {
            val field = getFieldByName(fieldUi.name) ?: return fieldUi
            if (!fieldUi.extractedValue.isValidCardNumber()) {
                return fieldUi.copy(errorText = field.errorText)
            }
            return fieldUi
        }

        fun validateDigitField(fieldUi: FieldUi.Digits): FieldUi {
            val field = getFieldByName(fieldUi.name) ?: return fieldUi
            if (field.regex?.matches(fieldUi.value) == false) {
                return fieldUi.copy(errorText = field.errorText)
            }
            return fieldUi
        }

        fun validateMoneyField(fieldUi: FieldUi.Money): FieldUi {
            if (fieldUi.amount == null || !validateAmount(
                    state,
                    fieldUi.amount,
                    fieldUi.currency
                )
            ) {
                return fieldUi.copy(
                    errorText = resourceProvider.getString(
                        R.string.feed_amount_error,
                        getFormattedSum(state.minUzs),
                        getFormattedSum(state.maxUzs)
                    )
                )
            }
            return fieldUi
        }

        fun validateMultilineField(fieldUi: FieldUi.Multiline): FieldUi {
            val field = getFieldByName(fieldUi.name) ?: return fieldUi

            if (field.regex?.matches(fieldUi.value) == false) {
                return fieldUi.copy(errorText = field.errorText)
            }
            return fieldUi
        }

        fun validatePhoneField(fieldUi: FieldUi.Phone): FieldUi {
            val field = getFieldByName(fieldUi.name) ?: return fieldUi

            if (!fieldUi.extractedValue.isValidUzPhoneNumber()) {
                return fieldUi.copy(errorText = field.errorText)
            }
            return fieldUi
        }

        fun validateSelectorField(fieldUi: FieldUi.Selector): FieldUi {
            if (fieldUi.value == null) {
                return fieldUi.copy(errorText = resourceProvider.getString(R.string.feed_selector_not_selected))
            }
            return fieldUi
        }

        fun validateTextField(fieldUi: FieldUi.Text): FieldUi {
            val field = getFieldByName(fieldUi.name) ?: return fieldUi

            if (field.regex?.matches(fieldUi.value) == false) {
                return fieldUi.copy(errorText = field.errorText)
            }
            return fieldUi
        }

        fun validateAll(fieldUi: FieldUi): FieldUi {
            return when (fieldUi) {
                is FieldUi.Card -> validateCardField(fieldUi)
                is FieldUi.Digits -> validateDigitField(fieldUi)
                is FieldUi.Money -> validateMoneyField(fieldUi)
                is FieldUi.Multiline -> validateMultilineField(fieldUi)
                is FieldUi.Phone -> validatePhoneField(fieldUi)
                is FieldUi.Selector -> validateSelectorField(fieldUi)
                is FieldUi.Text -> validateTextField(fieldUi)
            }
        }

        val newState = state.copy(uiFields = state.uiFields.map(::validateAll))
        if (newState.isAllFieldsCorrect) {
            ch.hideKeyboard.accept()
        }
        return newState
    }

    //region Fields Edited
    private fun onFieldEdited(state: FormState, event: Fields): FormState {
        return when (event) {
            is Fields.CardEdited -> onCardEdited(state, event)
            is Fields.DigitEdited -> onDigitEdited(state, event)
            is Fields.MoneyEdited -> state
            is Fields.AmountCalculated -> onMoneyEdited(state, event)
            is Fields.MultilineEdited -> onMultilineEdited(state, event)
            is Fields.PhoneEdited -> onPhoneEdited(state, event)
            is Fields.TextEdited -> onTextEdited(state, event)
        }
    }

    private fun onCardEdited(state: FormState, event: Fields.CardEdited): FormState {
        fun changeCardInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Card && fieldUi.name == event.field.name) {
                return fieldUi.copy(value = event.formatted, extractedValue = event.extracted)
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changeCardInfo))
    }

    private fun onDigitEdited(state: FormState, event: Fields.DigitEdited): FormState {
        fun changeDigitInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Digits && fieldUi.name == event.field.name) {
                return fieldUi.copy(value = event.value)
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changeDigitInfo))
    }

    private fun onMoneyEdited(state: FormState, event: Fields.AmountCalculated): FormState {
        fun changeMoneyInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Money && fieldUi.name == event.field.name) {
                if (event.convertedAmount == null) {
                    return fieldUi.copy(
                        amount = event.amount,
                        currency = event.currency,
                        helperText = EMPTY_STRING,
                        errorText = null
                    )
                }
                val resource = if (event.convertedCurrency == Currency.SUM) {
                    R.string.feed_amount_helper_sum
                } else {
                    R.string.feed_amount_helper_rub
                }
                val helperText = resourceProvider.getString(
                    resource, getFormattedSum(event.convertedAmount), state.rate
                )
                return fieldUi.copy(
                    amount = event.amount,
                    currency = event.currency,
                    helperText = helperText,
                    errorText = null
                )
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changeMoneyInfo))
    }

    private fun onMultilineEdited(state: FormState, event: Fields.MultilineEdited): FormState {
        fun changeMultilineInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Multiline && fieldUi.name == event.field.name) {
                return fieldUi.copy(value = event.value)
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changeMultilineInfo))
    }

    private fun onPhoneEdited(state: FormState, event: Fields.PhoneEdited): FormState {
        fun changePhoneInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Phone && fieldUi.name == event.field.name) {
                return fieldUi.copy(value = event.formatted, extractedValue = event.extracted)
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changePhoneInfo))
    }

    private fun onTextEdited(state: FormState, event: Fields.TextEdited): FormState {
        fun changeTextInfo(fieldUi: FieldUi): FieldUi {
            if (fieldUi is FieldUi.Text && fieldUi.name == event.field.name) {
                return fieldUi.copy(value = event.value)
            }
            return fieldUi
        }

        return state.copy(uiFields = state.uiFields.map(::changeTextInfo))
    }
    //endregion

    //region Mappers to Ui
    private fun Field.toCardUi(value: String): FieldUi.Card {
        return FieldUi.Card(
            name = name,
            hint = caption,
            helperText = description,
            value = value,
            extractedValue = value.filter(Char::isDigit)
        )
    }

    private fun Field.toDigitUi(value: String): FieldUi.Digits {
        return FieldUi.Digits(
            name = name,
            hint = caption,
            helperText = description,
            value = value
        )
    }

    private fun Field.toMoneyUi(): FieldUi.Money {
        return FieldUi.Money(
            name = name,
            helperText = description
        )
    }

    private fun Field.toMultilineUi(value: String): FieldUi.Multiline {
        return FieldUi.Multiline(
            name = name,
            hint = caption,
            helperText = description,
            value = value
        )
    }

    private fun Field.toPhoneUi(value: String): FieldUi.Phone {
        return FieldUi.Phone(
            name = name,
            helperText = description,
            value = value.takeIf { it.isNotEmpty() }?.let { "998$it" }?.formatUzPhone()
                ?: EMPTY_STRING,
            extractedValue = value
        )
    }

    private fun Field.toSelectorUi(value: String): FieldUi.Selector {
        return FieldUi.Selector(
            id = id,
            name = name,
            hint = caption,
            parent = relation,
            value = FieldValue(id = "", value = value)
        )
    }

    private fun Field.toTextUi(value: String): FieldUi.Text {
        return FieldUi.Text(
            name = name,
            hint = caption,
            helperText = description,
            value = value
        )
    }
    //endregion

    private fun validateAmount(state: FormState, amount: BigDecimal, currency: Currency): Boolean {
        val minUzs = state.minUzs
        val maxUzs = state.maxUzs
        val rate = state.rate
        val (minAmount, maxAmount) = if (currency == Currency.SUM) {
            minUzs to maxUzs
        } else {
            minUzs / rate to maxUzs / rate
        }

        return amount in minAmount..maxAmount
    }
}