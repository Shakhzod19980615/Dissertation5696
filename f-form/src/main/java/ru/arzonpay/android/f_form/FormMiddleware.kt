package ru.arzonpay.android.f_form

import io.reactivex.Observable
import ru.arzonpay.android.domain.Currency
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.domain.payment.FormItem
import ru.arzonpay.android.domain.payment.TRANSFER_AMOUNT_KEY
import ru.arzonpay.android.f_form.FormEvent.*
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.selector.SelectorBottomSheetRoute
import ru.arzonpay.android.i_config.ConfigInteractor
import ru.arzonpay.android.i_payment.PaymentInteractor
import ru.arzonpay.android.ui.analytics.event.form.*
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.extension.removeLast
import ru.arzonpay.android.ui.mvi.navigation.extension.replace
import ru.arzonpay.android.ui.mvi.navigation.extension.show
import ru.arzonpay.android.ui.navigation.routes.WebConfirmationFragmentRoute
import ru.arzonpay.android.ui.util.mvi.CLICK_DELAY
import ru.arzonpay.android.ui.util.mvi.withThrottle
import ru.surfstudio.android.analyticsv2.DefaultAnalyticService
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

@PerScreen
internal class FormMiddleware @Inject constructor(
    basePresenterDependency: BaseMiddlewareDependency,
    private val analyticsService: DefaultAnalyticService,
    private val navigationMiddleware: NavigationMiddleware,
    private val paymentInteractor: PaymentInteractor,
    private val sh: FormStateHolder,
    private val screenResultObserver: ScreenResultObserver,
    private val configInteractor: ConfigInteractor
) : BaseMiddleware<FormEvent>(basePresenterDependency) {

    private val state: FormState
        get() = sh.value

    override fun transform(eventStream: Observable<FormEvent>): Observable<out FormEvent> =
        transformations(eventStream) {
            addAll(
                Navigation::class decomposeTo navigationMiddleware,
                onCreate() eventMap { loadFormData() },
                onCreate() map { getConfigInfo() },
                Input.BackClicked::class mapTo { Navigation().removeLast() },
                Input.PayClicked::class filter { state.isAllFieldsCorrect }
                        map { ProcessPayment }
                        withThrottle CLICK_DELAY,
                ProcessPayment::class eventMapTo { sendForm() },
                SendFormRequest::class filter { it.isSuccess } map { openWebView(it.request.getData().uuid) },
                Input.SelectorClicked::class eventMapTo { loadFieldValues(it.field) },
                GetFieldValues::class filter { it.isSuccess } map {
                    openPicker(it.fieldId, it.request.getData())
                },
                Fields.MoneyEdited::class mapTo ::onMoneyChanged,
                observeSelectorResult(),

                FormRequest::class filter { it.isError } react { sendLoadErrorEvent() },
                Input.PayClicked::class reactTo { sendFormValidationEvent() },
                SendFormRequest::class filter { !it.isLoading } react {
                    sendFormRequestResultEvent(it.request)
                }
            )
        }

    private fun loadFormData(): Observable<out FormEvent> {
        return paymentInteractor.getForm(state.provider.id)
            .io()
            .asRequestEvent(::FormRequest)
    }

    private fun getConfigInfo(): FormEvent {
        val rate = configInteractor.getRate()
        val minUzs = configInteractor.getMinUzs()
        val maxUzs = configInteractor.getMaxUzs()
        return ConfigLoaded(rate, minUzs, maxUzs)
    }

    private fun sendForm(): Observable<out FormEvent> {
        val formItems = state.uiFields.map {
            val value = if (it.fieldName == TRANSFER_AMOUNT_KEY && it is FieldUi.Money) {
                configInteractor.convertAmount(it.amount, it.currency, Currency.SUM)?.toString()
                    ?: "0"
            } else {
                it.fieldValue
            }
            FormItem(
                name = it.fieldName,
                value = value
            )
        }
        return paymentInteractor.postForm(state.provider.id, formItems)
            .io()
            .asRequestEvent(::SendFormRequest)
    }

    private fun onMoneyChanged(event: Fields.MoneyEdited): FormEvent {
        val (converted, currency) = if (event.currency == Currency.RUB) {
            configInteractor.convertAmount(event.amount, event.currency, Currency.SUM) to
                    Currency.SUM
        } else {
            configInteractor.convertAmount(event.amount, event.currency, Currency.RUB) to
                    Currency.RUB
        }
        return Fields.AmountCalculated(
            event.field,
            event.amount,
            event.currency,
            converted,
            currency
        )
    }

    private fun loadFieldValues(field: FieldUi.Selector): Observable<out FormEvent> {
        val parentId = state.getParent(field.parent)
        if (field.parent != null && parentId == null) {
            return skip()
        }
        return paymentInteractor.getFieldValues(field.id, parentId)
            .io()
            .asRequestEvent { GetFieldValues(field.id, it) }
    }

    private fun openPicker(fieldID: String, values: List<FieldValue>): FormEvent {
        return Navigation().show(SelectorBottomSheetRoute(fieldID, values))
    }

    private fun openWebView(uuid: String): FormEvent {
        return Navigation().replace(WebConfirmationFragmentRoute(uuid, state.provider.name))
    }

    private fun observeSelectorResult(): Observable<out FormEvent> {
        return screenResultObserver
            .observeScreenResult(SelectorBottomSheetRoute(EMPTY_STRING, emptyList()))
            .map { FieldValueSelected(it.fieldId, it.fieldValue) }
    }

    private fun sendLoadErrorEvent() {
        analyticsService.performAction(LoadErrorEvent(state.provider.id))
    }

    private fun sendFormValidationEvent() {
        if (state.isAllFieldsCorrect) {
            analyticsService.performAction(FormFilledSuccessEvent(state.provider.id))
        } else {
            val incorrectFieldNames = state.uiFields.filter { !it.isValid }.map { it.fieldName }
            analyticsService.performAction(
                FormFilledErrorEvent(state.provider.id, incorrectFieldNames)
            )
        }
    }

    private fun sendFormRequestResultEvent(request: Request<*>) {
        if (request.isSuccess) {
            analyticsService.performAction(FormSendSuccessEvent(state.provider.id))
        } else {
            analyticsService.performAction(FormSendErrorEvent(state.provider.id))
        }
    }
}