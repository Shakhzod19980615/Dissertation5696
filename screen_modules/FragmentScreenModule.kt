package ru.arzonpay.android.ui.screen_modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.base_feature.BuildConfig
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForFragment
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstaller
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.provider.FragmentProvider
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForFragment
import ru.arzonpay.android.ui.error.ErrorHandlerModule
import ru.arzonpay.android.ui.message.MessageControllerImpl

@Module(includes = [ErrorHandlerModule::class])
class FragmentScreenModule(private val persistentScope: FragmentViewPersistentScope) : ScreenModule() {

    @Provides
    @PerScreen
    internal fun providePersistentScope(): ScreenPersistentScope {
        return persistentScope
    }

    @Provides
    @PerScreen
    internal fun provideScreenState(persistentScope: ScreenPersistentScope): ScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerScreen
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerScreen
    internal fun provideMessageController(
            activityProvider: ActivityProvider,
            fragmentProvider: FragmentProvider
    ): MessageController {
        return MessageControllerImpl(activityProvider, fragmentProvider)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentProvider(screenState: ScreenState): FragmentProvider {
        return FragmentProvider(screenState as FragmentScreenState)
    }

    @Provides
    @PerScreen
    internal fun provideDialogNavigator(
            activityProvider: ActivityProvider,
            fragmentProvider: FragmentProvider
    ): DialogNavigator {
        return DialogNavigatorForFragment(activityProvider, fragmentProvider, persistentScope)
    }

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(
            activityProvider: ActivityProvider,
            fragmentProvider: FragmentProvider,
            eventDelegateManager: ScreenEventDelegateManager,
            splitFeatureInstaller: SplitFeatureInstaller,
            isSplitFeatureModeOn: Boolean
    ): ActivityNavigator {
        return ActivityNavigatorForFragment(
                activityProvider,
                fragmentProvider,
                eventDelegateManager,
                splitFeatureInstaller,
                isSplitFeatureModeOn
        )
    }

    @Provides
    @PerScreen
    internal fun provideSplitFeatureInstaller(context: Context): SplitFeatureInstaller {
        return SplitFeatureInstaller(context)
    }

    @Provides
    @PerScreen
    internal fun provideIsSplitFeatureModeOn(): Boolean {
        return !BuildConfig.DEBUG
    }
}