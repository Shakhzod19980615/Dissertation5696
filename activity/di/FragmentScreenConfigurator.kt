package ru.arzonpay.android.ui.activity.di

import android.os.Bundle
import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentView
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface
import ru.arzonpay.android.ui.screen_modules.FragmentScreenModule

abstract class FragmentScreenConfigurator(
        args: Bundle?
) : BaseFragmentViewConfigurator<ActivityComponent, FragmentScreenModule>(args) {

    override fun getFragmentScreenModule(): FragmentScreenModule {
        return FragmentScreenModule(persistentScope)
    }

    override fun getParentComponent(): ActivityComponent {
        return (getTargetFragmentView<CoreFragmentView>().activity as CoreActivityInterface)
                .persistentScope
                .configurator
                .activityComponent as ActivityComponent
    }
}