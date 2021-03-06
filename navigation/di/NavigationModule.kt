package ru.arzonpay.android.ui.navigation.di

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.executor.screen.activity.ActivityCommandWithResultExecutor
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultFactory
import ru.surfstudio.android.navigation.observer.storage.RouteStorage
import ru.surfstudio.android.navigation.observer.storage.ScreenResultStorage
import ru.surfstudio.android.navigation.observer.storage.file.FileRouteStorage
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import javax.inject.Named

@Module
object NavigationModule {

    @Provides
    @PerApplication
    @Named(SCREEN_ROUTE_STORAGE_DIR)
    internal fun provideRouteStorageDir(context: Context): String {
        return ContextCompat.getNoBackupFilesDir(context)!!.absolutePath
    }

    @Provides
    @PerApplication
    internal fun provideRouteStorage(
        @Named(SCREEN_ROUTE_STORAGE_DIR) dir: String
    ): RouteStorage {
        return FileRouteStorage(dir)
    }

    @Provides
    @PerApplication
    @Named(SCREEN_RESULT_STORAGE_DIR)
    internal fun provideStorage(context: Context): String {
        return ContextCompat.getNoBackupFilesDir(context)!!.absolutePath
    }

    @Provides
    @PerApplication
    internal fun provideScreenResultStorage(
        @Named(SCREEN_RESULT_STORAGE_DIR) dir: String
    ): ScreenResultStorage {
        return FileScreenResultStorage(dir)
    }

    @Provides
    @PerApplication
    fun provideScreenResultBus(storage: ScreenResultStorage): ScreenResultBus {
        return ScreenResultBus(storage)
    }

    @Provides
    @PerApplication
    internal fun provideNavigatorFactory(
        storage: RouteStorage,
        emitter: ScreenResultEmitter
    ): ActivityNavigatorWithResultFactory {
        return ActivityNavigatorWithResultFactory(emitter, storage)
    }

    @Provides
    @PerApplication
    internal fun provideActivityNavigationProviderCallbacks(
        factory: ActivityNavigatorWithResultFactory
    ): ActivityNavigationProviderCallbacks {
        return ActivityNavigationProviderCallbacks(activityNavigatorFactory = factory)
    }

    @Provides
    @PerApplication
    fun provideActivityNavigationProvider(
        activityNavigationProviderCallbacks: ActivityNavigationProviderCallbacks
    ): ActivityNavigationProvider {
        return activityNavigationProviderCallbacks
    }

    @Provides
    @PerApplication
    internal fun provideNavigatorCommand(
        screenResultEmitter: ScreenResultEmitter,
        activityNavigationProvider: ActivityNavigationProvider
    ): AppCommandExecutorWithResult {
        return AppCommandExecutorWithResult(
            screenResultEmitter,
            activityNavigationProvider
        )
    }

    @Provides
    @PerApplication
    fun provideCommandExecutor(commandExecutor: AppCommandExecutorWithResult): AppCommandExecutor =
        commandExecutor

    @Provides
    @PerApplication
    fun provideScreenResultObserver(screenResultBus: ScreenResultBus): ScreenResultObserver {
        return screenResultBus
    }

    @Provides
    @PerApplication
    fun provideScreenResultEmitter(screenResultBus: ScreenResultBus): ScreenResultEmitter {
        return screenResultBus
    }

    private const val SCREEN_RESULT_STORAGE_DIR = "screen_result_storage"
    private const val SCREEN_ROUTE_STORAGE_DIR = "screen_route_storage"
}
