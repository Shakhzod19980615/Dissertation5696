package ru.arzonpay.android.f_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.arzonpay.android.domain.payment.Status
import ru.arzonpay.android.f_details.TransferDetailsEvent.Input
import ru.arzonpay.android.f_details.controller.TransferAmountItemController
import ru.arzonpay.android.f_details.controller.TransferInfoItemController
import ru.arzonpay.android.f_details.controller.TransferStatusItemController
import ru.arzonpay.android.f_details.data.FieldUi
import ru.arzonpay.android.f_details.databinding.FragmentTransferDetailsBinding
import ru.arzonpay.android.f_details.decor.BackgroundOverlayDecor
import ru.arzonpay.android.f_details.di.TransferDetailsScreenConfigurator
import ru.arzonpay.android.ui.mvi.view.BaseMviFragmentView
import ru.arzonpay.android.ui.placeholder.LoadStateView
import ru.arzonpay.android.ui.placeholder.loadstate.renderer.DefaultLoadStateRenderer
import ru.arzonpay.android.ui.recylcer.decoration.SimpleOffsetDecorator
import ru.arzonpay.android.ui.util.back_press.addDefaultOnBackPressedCallback
import ru.arzonpay.android.ui.view.extensions.getThemeColor
import ru.arzonpay.android.ui.view.extensions.toPx
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.surfstudio.android.recycler.decorator.easyadapter.underlay
import ru.surfstudio.android.utilktx.ktx.ui.view.actionIfChanged
import javax.inject.Inject

internal class TransferDetailsFragmentView :
    BaseMviFragmentView<TransferDetailsState, TransferDetailsEvent>(),
    CrossFeatureFragment,
    LoadStateView {

    @Inject
    override lateinit var hub: ScreenEventHub<TransferDetailsEvent>

    @Inject
    override lateinit var sh: TransferDetailsStateHolder

    override var renderer: DefaultLoadStateRenderer? = null

    private val binding by viewBinding(FragmentTransferDetailsBinding::bind)

    private val adapter = EasyAdapter()
    private val infoController = TransferInfoItemController()
    private val amountController = TransferAmountItemController()
    private val statusController = TransferStatusItemController()

    override fun createConfigurator() = TransferDetailsScreenConfigurator(requireArguments())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transfer_details, container, false)
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        renderer = null
        super.onDestroyView()
    }

    override fun initViews() = with(binding) {
        renderer = DefaultLoadStateRenderer(placeholderContainer)
        fieldsRv.adapter = adapter
        fieldsRv.addItemDecoration(createDecorator())
        initListeners()
    }

    override fun render(state: TransferDetailsState) {
        renderLoadState(state.loadState)
        renderList(state.uiFields)
        binding.toolbar.actionIfChanged(state.transaction) { _ ->
            binding.toolbar.title = getString(R.string.transfer_details_title, state.transaction.id)
        }
        renderButton(state.transaction.status)
    }

    private fun initListeners() = with(binding) {
        toolbar.setNavigationOnClickListener { Input.BackClicked.emit() }
        addDefaultOnBackPressedCallback { Input.BackClicked.emit() }
        replyBtn.setOnClickListener { Input.ReplyClicked.emit() }
        supportBtn.setOnClickListener { Input.SupportClicked.emit() }
    }

    private fun createDecorator(): RecyclerView.ItemDecoration {
        val draw =
            BackgroundOverlayDecor(requireContext().getThemeColor(R.attr.inputBackgroundColor))

        val offset =
            SimpleOffsetDecorator(startOffset = 24.toPx, endOffset = 24.toPx, bottomOffset = 2.toPx)

        return Decorator.Builder()
            .offset(offset)
            .underlay(infoController.viewType() to draw)
            .underlay(amountController.viewType() to draw)
            .build()
    }

    private fun renderList(uiFiles: List<FieldUi>) {
        val itemList = ItemList.create()
        uiFiles.forEach { fieldUi ->
            when (fieldUi) {
                is FieldUi.Info -> itemList.add(fieldUi, infoController)
                is FieldUi.Amount -> itemList.add(fieldUi, amountController)
                is FieldUi.Status -> itemList.add(fieldUi, statusController)
            }
        }
        adapter.setItems(itemList)
    }

    private fun renderButton(status: Status) = with(binding) {
        val isRetryVisible = status == Status.CREATED || status == Status.SUCCESS
        replyBtn.isVisible = isRetryVisible
        supportBtn.isVisible = !isRetryVisible
    }
}