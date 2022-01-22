package ru.arzonpay.android.ui.mvi.mapper

import ru.arzonpay.android.i_network.error.NonAuthorizedException
import ru.arzonpay.android.i_network.network.error.NoInternetException
import ru.arzonpay.android.ui.placeholder.LoadStateType
import ru.surfstudio.android.core.mvi.ui.mapper.RequestDataMapper
import ru.surfstudio.android.core.mvi.ui.mapper.RequestErrorHandler
import ru.surfstudio.android.core.mvi.ui.mapper.RequestLoadingMapper
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SimpleLoading
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * Singleton-фабрика мапперов запросов.
 *
 * Используется для хранения переиспользуемых мапперов внутри проекта.
 * */
object RequestMappers {

    /**
     * Мапперы данных запроса.
     * */
    val data = DataMappers

    /**
     * Мапперы состояния загрузки запроса.
     * */
    val loading = LoadingMappers

    /**
     * Обработчики ошибок запроса.
     * */
    val error = ErrorHandlers

    /**
     * Мапперы данных запроса.
     * */
    object DataMappers {

        /**
         * ## Маппер данных одиночного запроса.
         *
         * Маппер каждый новый запрос "очищает" данные в стейте.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Сделать одиночный запрос и узнать результат его выполнения,
         * при этом нам не важно предыдущее его состояние.
         *
         * @return только что полученные данные из запроса.
         * */
        fun <T> single(): RequestDataMapper<T, T, T> = { request, _ -> request.getDataOrNull() }

        /**
         * ## Маппер данных стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * При первом запросе получить данные и сохранить их в стейт;
         * * При последующих запросах, если они удачные - обновить данные в стейте.
         *
         * @return только что полученные данные из запроса, либо данные из стейта.
         * */
        fun <T> default(): RequestDataMapper<T, T, T> =
            { request, data -> request.getDataOrNull() ?: data }

        /**
         * ## Маппер данных списка
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * При получении данных сохранять их только в том случае если они не пустые.
         * При первом запросе получить данные и сохранить их в стейт;
         * При последующих запросах если не получили данные вернуть старые данные, если получили пустое значение
         * то удалить сохранные данные
         *
         * @return только что полученные данные из запроса (если придет пустой список то null), либо данные из стейта.
         */
        fun <T> emptyListToNull(): RequestDataMapper<List<T>, List<T>, List<T>> = { request, data ->
            if (request.getDataOrNull() == null) data
            else request.getData().takeIf { it.isNotEmpty() }
        }

        /**
         * ## Маппер данных запроса.
         *
         * При ошибке обнуляет данные, в остальном рабоает аналогично `default()`-мапперу.
         *
         * Подробное поведение:
         * * Если запрос **выполняется впервые, либо после ошибки** -- данные отсутствуют (`null`).
         * * Если запрос **выполнился успешно** -- данные сохраняются (`T`).
         * * Если запрос **выполняется повторно, после успеха** -- данные присутствуют (`T`).
         * * Если запрос **выполнился с ошибкой** -- данные удаляются (`null`).
         * */
        fun <T> keepUntilError(): RequestDataMapper<T, T, T> = { request, uiData ->
            when {
                request.isError -> null
                else -> request.getDataOrNull() ?: uiData
            }
        }

        /**
         * ## Маппер данных пагинируемого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * При первом запросе получить пагинируемые данные и
         * сохранить их в стейт с преобразованием в `PaginationBundle`;
         * * При последующих запросах, если они удачные - дополнить/обновить данные в стейте;
         *
         * @return только что полученные данные из запроса (опционально смерженные с данными из стейта),
         *         либо данные из стейта.
         * */
        fun <T> pagination(
            isSwr: Boolean = false
        ): RequestDataMapper<DataList<T>, PaginationBundle<T>, PaginationBundle<T>> =
            { request, paginationBundle ->
                val currentDataList = paginationBundle?.data?.let {
                    DataList(it, it.limit, it.offset, it.totalCount)
                }
                val newDataList = request.getDataOrNull()

                val hasCurrentData = currentDataList != null
                val hasNewData = newDataList != null

                val isNewDataListHasOffset = (newDataList?.offset ?: 0) != 0
                val canMergeLists = hasNewData && isNewDataListHasOffset && hasCurrentData

                val mappedDataList = when {
                    canMergeLists -> currentDataList?.merge(newDataList)
                    hasNewData -> newDataList
                    else -> currentDataList
                }

                val hasMoreData = mappedDataList?.canGetMore() ?: false
                val mappedPaginationState = when {
                    request.isSuccess && !hasMoreData || request.isError && isSwr -> PaginationState.COMPLETE
                    !request.isError && hasMoreData -> PaginationState.READY
                    request.isError -> PaginationState.ERROR
                    else -> null
                }

                PaginationBundle(mappedDataList, mappedPaginationState)
            }
    }

    /**
     * Мапперы состояния загрузки запроса.
     * */
    object LoadingMappers {

        /**
         * ## Маппер состояния загрузки простого запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * Получить состояние загрузки запроса в простом формате (загружается/не загружается).
         * * На экране осуществляется несколько запросов, на основании состояния загрузки которых
         * вычисляется все состояние экрана.
         *
         * @return актуальное, простое, состояние загрузки.
         * */
        fun <T1, T2> simple(): RequestLoadingMapper<T1, T2> =
            { request, _ -> SimpleLoading(request.isLoading) }

        /**
         * ## Маппер состояния загрузки стандартного запроса.
         *
         * Следует использовать этот маппер в тех случаях, когда нам необходимо:
         *
         * * На основании одного запроса - вычислить состояние всего экрана.
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> default(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
            { request, data ->
                val isPagination = data is PaginationBundle<*>
                val hasData = when (data) {
                    is Optional<*> -> data.hasValue
                    is List<*> -> data.isNotEmpty()
                    is PaginationBundle<*> -> data.hasData
                    null -> false
                    else -> true
                }
                when {
                    request.isLoading && hasData && isSwr -> LoadStateType.None(isSwrLoading = true)
                    request.isLoading && hasData && !isPagination -> LoadStateType.Transparent
                    request.isLoading && !hasData -> LoadStateType.Main
                    request.isError && !hasData -> LoadStateType.Error()
                    request.isSuccess && !hasData -> LoadStateType.Empty
                    else -> LoadStateType.None(isSwrLoading = false)
                }
            }

        /**
         * ## Маппер состояния загрузки запроса.
         *
         * Подробное поведение:
         * * Запрос загружается с `isSwr`-флагом -- будет применен [LoadStateType.None] с `isSwrLoading == true`;
         * * Запрос загружается -- будет применен [LoadStateType.Main];
         * * иначе [LoadStateType.None].
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> mainOrNone(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
            { request, _ ->
                when {
                    request.isLoading && isSwr -> LoadStateType.None(isSwrLoading = true)
                    request.isLoading -> LoadStateType.Main
                    else -> LoadStateType.None(isSwrLoading = false)
                }
            }

        /**
         * ## Маппер состояния загрузки запроса.
         *
         * Подробное поведение:
         * * Запрос загружается с `isSwr`-флагом -- будет применен [LoadStateType.None] с `isSwrLoading == true`;
         * * Запрос загружается -- будет применен [LoadStateType.Transparent];
         * * иначе [LoadStateType.None].
         *
         * @return актуальное состояние загрузки.
         */
        fun <T1, T2> transparentOrNone(isSwr: Boolean = false): RequestLoadingMapper<T1, T2> =
            { request, _ ->
                when {
                    request.isLoading && isSwr -> LoadStateType.None(isSwrLoading = true)
                    request.isLoading -> LoadStateType.Transparent
                    else -> LoadStateType.None(isSwrLoading = false)
                }
            }
    }

    /**
     * Обработчики ошибок запроса.
     * */
    object ErrorHandlers {

        /**
         * ## Обработчик ошибок. Форсированный.
         *
         * Каждую из ошибок отправляет в [ErrorHandler] и завершает обработку ошибок для запроса.
         *
         * Следует использовать этот маппер в тех случая, когда нам необходимо:
         *
         * * Обработать все возникающие ошибки в [ErrorHandler]'е (например,
         * для одиночного запроса, у которого нету UI репрезентации).
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> forced(errorHandler: ErrorHandler): RequestErrorHandler<T> = { error, _, _ ->
            error?.also(errorHandler::handleError)
            true
        }

        /**
         * ## Обработчик ошибок. Обработка в зависимости от текущего [LoadStateType].
         *
         * Обрабатывает ошибки только в том случае, когда экран не находится в состоянии ошибки.
         *
         * **Ошибки [NonAuthorizedException] и [NoInternetException] -- обрабатываются всегда!**
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> loadingBased(errorHandler: ErrorHandler): RequestErrorHandler<T> =
            { error, _, loading ->
                val isNonAuthorized = error is NonAuthorizedException
                val isNoInternet = error is NoInternetException
                val isErrorStateOnDisplay = loading is LoadStateType.Error
                if (isNonAuthorized || isNoInternet || !isErrorStateOnDisplay) error?.also(
                    errorHandler::handleError
                )
                true
            }

        /**
         * ## Обработчик ошибок. Обрабатывает только разлогин и отсутствие интернета.
         *
         * Обрабатывает только ошибки [NonAuthorizedException] и [NoInternetException], остальные -- игнорируются.
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> noInternet(errorHandler: ErrorHandler): RequestErrorHandler<T> = { error, _, _ ->
            val isNonAuthorized = error is NonAuthorizedException
            val isNoInternet = error is NoInternetException
            if (isNonAuthorized || isNoInternet) error?.also(errorHandler::handleError)
            isNonAuthorized || isNoInternet
        }

        /**
         * ## Обработчик ошибок. Обрабатывает только разлогин.
         *
         * Обрабатывает только ошибки [NonAuthorizedException], остальные -- игнорируются.
         *
         * @return Была ли обработана ошибка? (Всегда -- true)
         * */
        fun <T> nonAuthorized(errorHandler: ErrorHandler): RequestErrorHandler<T> = { error, _, _ ->
            val isNonAuthorized = error is NonAuthorizedException
            if (isNonAuthorized) error?.also(errorHandler::handleError)
            true
        }
    }
}
