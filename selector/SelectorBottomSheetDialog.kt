package ru.arzonpay.android.f_form.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.arzonpay.android.domain.payment.FieldValue
import ru.arzonpay.android.f_form.R
import ru.arzonpay.android.f_form.databinding.DialogSelectorBinding
import ru.arzonpay.android.f_form.selector.controller.ValueItemController
import ru.arzonpay.android.ui.dialog.base.result.BaseResultBottomDialogView
import ru.arzonpay.android.ui.recylcer.decoration.Gap
import ru.arzonpay.android.ui.recylcer.decoration.LinearDividerDrawer
import ru.arzonpay.android.ui.recylcer.decoration.rule.Rules
import ru.arzonpay.android.ui.view.extensions.getThemeColor
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator

internal class SelectorBottomSheetDialog : BaseResultBottomDialogView<SelectorResult>() {

    override lateinit var route: SelectorBottomSheetRoute

    override fun getName() = "SelectorBottomSheetDialog"

    private val binding by viewBinding(DialogSelectorBinding::bind)
    private val adapter = EasyAdapter()
    private val valueController = ValueItemController {
        closeWithResult(SelectorResult(route.fieldId, it))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_selector, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        route = SelectorBottomSheetRoute(requireArguments())
        initialize()
    }

    private fun initialize() = with(binding) {
        valuesRv.adapter = adapter
        val gap = Gap(
            color = requireContext().getThemeColor(R.attr.dividerColor),
            height = 1.toPx,
            paddingStart = 24.toPx,
            paddingEnd = 24.toPx,
            paddingTop = 1.toPx,
            rule = Rules.MIDDLE
        )
        val dividerDecor = LinearDividerDrawer(gap)
        valuesRv.addItemDecoration(
            Decorator.Builder()
                .overlay(valueController.viewType() to dividerDecor)
                .build()
        )
        adapter.setItems(ItemList.create(route.fieldValues, valueController))
    }
}