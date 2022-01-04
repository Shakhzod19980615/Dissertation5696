package ru.arzonpay.android.f_form

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import ru.arzonpay.android.f_form.FormEvent.Fields
import ru.arzonpay.android.f_form.FormEvent.Input
import ru.arzonpay.android.f_form.controllers.*
import ru.arzonpay.android.f_form.data.FieldUi
import ru.arzonpay.android.f_form.databinding.FragmentFormBinding
import ru.arzonpay.android.f_form.di.FormScreenConfigurator
import ru.arzonpay.android.ui.mvi.view.BaseMviFragmentView
import ru.arzonpay.android.ui.placeholder.LoadStateView
import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.recylcer.decoration.SimpleOffsetDecorator
import ru.arzonpay.android.ui.util.back_press.hideKeyboard
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import javax.inject.Inject

internal class FormFragmentView : BaseMviFragmentView<FormState, FormEvent>(),
    CrossFeatureFragment,
    LoadStateView {

    @Inject
    override lateinit var hub: ScreenEventHub<FormEvent>

    @Inject
    override lateinit var sh: FormStateHolder

    override var renderer: DefaultLoadStateRenderer? = null

    @Inject
    lateinit var ch: FormCommandHolder

    private val binding by viewBinding(FragmentFormBinding::bind)
    private val adapter = EasyAdapter()
    private val cardController = CardInputItemController { field, formatted, extracted ->
        Fields.CardEdited(field, formatted, extracted).emit()
    }
    private val digitController = DigitInputItemController { field, value ->
        Fields.DigitEdited(field, value).emit()
    }
    private val moneyController = MoneyInputItemController { field, amount, currency ->
        Fields.MoneyEdited(field, amount, currency).emit()
    }
    private val multilineController = MultilineInputItemController { field, value ->
        Fields.MultilineEdited(field, value).emit()
    }
    private val phoneController = PhoneInputItemController { field, formatted, extracted ->
        Fields.PhoneEdited(field, formatted, extracted).emit()
    }
    private val selectorController = SelectorInputItemController {
        Input.SelectorClicked(it).emit()
    }
    private val textController = TextInputItemController { field, value ->
        Fields.TextEdited(field, value).emit()
    }

    override fun createConfigurator() = FormScreenConfigurator(requireArguments())

    override fun getScreenName() = "FormFragmentView"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        renderer = null
        super.onDestroyView()
    }

    override fun initViews() {
        renderer = DefaultLoadStateRenderer(binding.placeholderContainer)
        ch.hideKeyboard bindTo { hideKeyboard() }
        initListeners()
        initRv()
    }

    override fun render(state: FormState) {
        binding.toolbar.title = state.provider.name
        renderLoadState(state.loadState)
        renderList(state.uiFields)
    }

    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener { Input.BackClicked.emit() }
        binding.continueBtn.setOnClickListener { Input.PayClicked.emit() }
    }

    private fun initRv() {
        binding.fieldsRv.adapter = adapter
        val offset = SimpleOffsetDecorator(bottomOffset = 16.toPx)
        binding.fieldsRv.addItemDecoration(Decorator.Builder().offset(offset).build())
        (binding.fieldsRv.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
    }

    private fun renderList(fields: List<FieldUi>) {
        val itemList = ItemList.create()
        for (field in fields) {
            when (field) {
                is FieldUi.Card -> itemList.add(field, cardController)
                is FieldUi.Digits -> itemList.add(field, digitController)
                is FieldUi.Money -> itemList.add(field, moneyController)
                is FieldUi.Multiline -> itemList.add(field, multilineController)
                is FieldUi.Phone -> itemList.add(field, phoneController)
                is FieldUi.Selector -> itemList.add(field, selectorController)
                is FieldUi.Text -> itemList.add(field, textController)
            }
        }

        adapter.setItems(itemList)
    }
}