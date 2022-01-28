package ru.arzonpay.android.f_details

import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.domain.payment.Transaction
import ru.arzonpay.android.f_details.data.FieldUi
import ru.arzonpay.android.ui.navigation.routes.TransferDetailsFragmentRoute
import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class TransferDetailsState(
    val transaction: Transaction,
    val fields: List<Field>? = null,
    val uiFields: List<FieldUi> = emptyList(),
    val loadState: LoadStateType = LoadStateType.Main
)

@PerScreen
internal class TransferDetailsStateHolder @Inject constructor(
    route: TransferDetailsFragmentRoute
) : State<TransferDetailsState>(TransferDetailsState(route.transaction))