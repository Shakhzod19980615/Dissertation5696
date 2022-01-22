package ru.arzonpay.android.ui.activity.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.arzonpay.android.base_feature.BuildConfig
import ru.arzonpay.android.ui.message.MessageControllerImpl
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstaller
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.ActivityScreenState
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.rxbus.RxBus

/**
 * Модуль для dagger Activity Component
 * поставляет ряд сущностей, например навигаторы, причем они находятся в @PerActivity scope
 * и не пробрасываются в дочерние scope, эти обьекты могут быть использованы без презентера,
 * например открытие необходимого фрагмента с помощью FragmentNavigator из активити контейнера.
 * Эти обьекты могут также использоваться внутри дополнительных обектов со специфической логикой,
 * принадлежащих скоупу @PerScreen
 */
@Module
class ActivityModule(private val persistentScope: ActivityPersistentScope) {

    @Provides
    @PerActivity
    internal fun provideActivityPersistentScope(): ActivityPersistentScope {
        return persistentScope
    }

    @Provides
    @PerActivity
    internal fun providePersistentScope(persistentScope: ActivityPersistentScope): PersistentScope {
        return persistentScope
    }

    @Provides
    @PerActivity
    internal fun provideActivityScreenState(): ActivityScreenState {
        return persistentScope.screenState
    }

    @Provides
    @PerActivity
    internal fun provideActivityProvider(): ActivityProvider {
        return ActivityProvider(persistentScope.screenState)
    }

    @Provides
    @PerActivity
    internal fun provideActivityNavigator(
            activityProvider: ActivityProvider,
            eventDelegateManager: ScreenEventDelegateManager,
            splitFeatureInstaller: SplitFeatureInstaller,
            isSplitFeatureModeOn: Boolean
    ): ActivityNavigator {
        return ActivityNavigatorForActivity(
                activityProvider,
                eventDelegateManager,
                splitFeatureInstaller,
                isSplitFeatureModeOn
        )
    }

    @Provides
    @PerActivity
    internal fun provideSplitFeatureInstaller(context: Context): SplitFeatureInstaller {
        return SplitFeatureInstaller(context)
    }

    @Provides
    @PerActivity
    internal fun provideIsSplitFeatureModeOn(): Boolean {
        return !BuildConfig.DEBUG
    }

    @Provides
    @PerActivity
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }

    @Provides
    @PerActivity
    internal fun provideEventDelegateManager(): ScreenEventDelegateManager {
        return persistentScope.screenEventDelegateManager
    }

    @Provides
    @PerActivity
    internal fun provideMessageController(activityProvider: ActivityProvider): MessageController {
        return MessageControllerImpl(activityProvider)
    }

    @Provides
    @PerActivity
    internal fun provideTabFragmentNavigator(
            activityProvider: ActivityProvider,
            eventDelegateManager: ScreenEventDelegateManager
    ): TabFragmentNavigator {
        return TabFragmentNavigator(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerActivity
    internal fun provideRxBus(): RxBus {
        return RxBus()
    }
}