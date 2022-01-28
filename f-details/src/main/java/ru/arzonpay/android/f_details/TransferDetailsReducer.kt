package ru.arzonpay.android.f_details

import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.TRANSFER_AMOUNT_KEY
import ru.arzonpay.android.f_details.TransferDetailsEvent.FormRequestEvent
import ru.arzonpay.android.f_details.data.FieldUi
import ru.arzonpay.android.ui.mvi.mapper.RequestMappers
import ru.arzonpay.android.ui.util.getFormattedSum
import ru.arzonpay.android.ui.util.getFormattedSumWithSom
import ru.arzonpay.android.ui.util.loadStateType
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvi.impls.ui.reducer.BaseReducer
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import java.math.RoundingMode
import javax.inject.Inject

@PerScreen
internal class TransferDetailsReducer @Inject constructor(
    dependency: BaseReactorDependency,
    private val resourceProvider: ResourceProvider
) : BaseReducer<TransferDetailsEvent, TransferDetailsState>(dependency) {

    override fun reduce(
        state: TransferDetailsState,
        event: TransferDetailsEvent
    ): TransferDetailsState {
        return when (event) {
            is FormRequestEvent -> onFormRequest(state, event)
            else -> state
        }
    }

    private fun onFormRequest(
        state: TransferDetailsState,
        event: FormRequestEvent
    ): TransferDetailsState {
        val newRequest = RequestMapper.builder(event.request)
            .mapData(RequestMappers.data.single())
            .mapLoading(RequestMappers.loading.default())
            .handleError(RequestMappers.error.loadingBased(errorHandler))
            .build()

        return buildUiFields(
            state.copy(
                fields = newRequest.data,
                loadState = newRequest.loadStateType
            )
        )
    }

    private fun buildUiFields(state: TransferDetailsState): TransferDetailsState {
        state.fields ?: return state
        val uiFields = mutableListOf<FieldUi>()
        //особое отображение для суммы
        val formItems = state.transaction.fields.filter { it.name != TRANSFER_AMOUNT_KEY }
        val amount = state.transaction.fields
            .firstOrNull { it.name == TRANSFER_AMOUNT_KEY }
            ?.value?.toBigDecimalOrNull()

        for (item in formItems) {
            val formField = state.fields.firstOrNull { it.name == item.name } ?: continue
            uiFields.add(
                FieldUi.Info(
                    formField.caption,
                    item.value
                )
            )
        }
        if (amount != null) {
            uiFields.add(
                FieldUi.Amount(
                    resourceProvider.getString(R.string.feed_amount),
                    getFormattedSum(
                        amount.divide(state.transaction.rate, 2, RoundingMode.HALF_EVEN)
                    ).toString(),
                    Currency.RUB
                )
            )
            uiFields.add(
                FieldUi.Info(
                    resourceProvider.getString(R.string.transfer_details_amount_receive),
                    getFormattedSumWithSom(amount).toString()
                )
            )
        }

        uiFields.add(FieldUi.Status(state.transaction.status))

        return state.copy(uiFields = uiFields)
    }
}