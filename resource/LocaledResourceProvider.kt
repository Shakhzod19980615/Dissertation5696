package ru.arzonpay.android.ui.resource

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

@PerApplication
class LocaledResourceProvider @Inject constructor(
    private val contextProvider: LocaleContextProvider
) : ResourceProvider {

    private val context: Context
        get() = contextProvider.get()

    override fun getString(@StringRes id: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getString(id)
        } else {
            context.resources.getString(id, *args)
        }
    }

    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg args: Any): String {
        return if (args.isEmpty()) {
            context.resources.getQuantityString(id, quantity)
        } else {
            context.resources.getQuantityString(id, quantity, *args)
        }
    }

    override fun getStringList(@ArrayRes id: Int): List<String> {
        return context.resources.getStringArray(id).toList()
    }

    override fun getDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableRes)
    }

    override fun getDimen(@DimenRes dimenRes: Int): Int {
        return context.resources.getDimensionPixelOffset(dimenRes)
    }

    override fun getInteger(@IntegerRes integerRes: Int): Int {
        return context.resources.getInteger(integerRes)
    }
}