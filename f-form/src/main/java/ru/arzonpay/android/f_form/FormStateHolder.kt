package ru.arzonpay.android.f_form

import ru.arzonpay.android.domain.payment.Field
import ru.arzonpay.android.domain.payment.FormItem
import ru.arzonpay.android.domain.payment.Provider
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.ui.navigation.routes.FormFragmentRoute
import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.dagger.scope.PerScreen
import java.math.BigDecimal
import javax.inject.Inject

internal data class FormState(
    val provider: Provider,
    val formItems: List<FormItem>,
    val loadState: LoadStateType = LoadStateType.Main,
    val uiFields: List<FieldUi> = emptyList(),
    val fields: List<Field> = emptyList(),
    val rate: BigDecimal = BigDecimal.ZERO,
    val minUzs: BigDecimal = BigDecimal.ZERO,
    val maxUzs: BigDecimal = BigDecimal.ZERO
) {
    val isAllFieldsCorrect: Boolean
        get() = uiFields.all(FieldUi::isValid)

    fun getParent(parent: String?): String? {
        return uiFields.filterIsInstance<FieldUi.Selector>()
            .firstOrNull { it.name == parent }?.value?.id
            ?.takeIf(String::isNotEmpty)
    }
}

/**
 * State Holder [FormFragmentView]
 */
@PerScreen
internal class FormStateHolder @Inject constructor(
    route: FormFragmentRoute
) : State<FormState>(
    FormState(route.provider, route.items)
)

@PerScreen
internal class FormCommandHolder @Inject constructor() {
    val hideKeyboard = Command<Unit>()
}